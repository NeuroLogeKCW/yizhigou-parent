//控制层
app.controller('goodsController', function ($scope, $controller,$location, goodsService, uploadService, itemCatService, typeTemplateService) {

    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne=function(){
        var id= $location.search()['id'];//获取参数值
        if(id==null){
            return ;
        }
        goodsService.findOne(id).success(
            function(response){
                $scope.entity= response;
                //富文本编辑器的回显
                editor.html($scope.entity.tbGoodsDesc.introduction);
                //扩展属性的回显
                $scope.entity.tbGoodsDesc.customAttributeItems=  JSON.parse($scope.entity.tbGoodsDesc.customAttributeItems);
                //规格的回显
                $scope.entity.tbGoodsDesc.specificationItems=JSON.parse($scope.entity.tbGoodsDesc.specificationItems);
                //图片回显
                $scope.entity.tbGoodsDesc.itemImages=JSON.parse($scope.entity.tbGoodsDesc.itemImages);
                //SKU列表的回显
                for (var i=0;i<$scope.entity.itemList.length;i++){
                    $scope.entity.itemList[i].spec =  JSON.parse($scope.entity.itemList[i].spec);
                }

            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        alert($scope.entity.tbGoods.id)
        if($scope.entity.tbGoods.id!=null){//如果有ID
            serviceObject=goodsService.update( $scope.entity ); //修改
        }else{
            serviceObject=goodsService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function (response) {
                if (response.success) {
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }


    //批量删除
    $scope.dele = function () {
        //获取选中的复选框
        goodsService.dele($scope.selectIds).success(
            function (response) {
                if (response.success) {
                    $scope.reloadList();//刷新列表
                    $scope.selectIds = [];
                }
            }
        );
    }

    $scope.searchEntity = {};//定义搜索对象

    //搜索
    $scope.search = function (page, rows) {
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //保存
    $scope.entity = null;
    $scope.add = function () {
        //alert($scope.entity.tbGoods.goodsName)
        //alert($scope.entity.tbGoodsDesc.saleService)
        $scope.entity.tbGoodsDesc.introduction = editor.html();
        goodsService.add($scope.entity).success(
            function (response) {
                if (response.success) {
                    alert('保存成功');
                    $scope.entity = {};
                    editor.html('');//清空富文本编辑器
                } else {
                    alert(response.message);
                }
            }
        );
    }

    /**
     * 上传图片
     */

    $scope.uploadFile = function () {
        uploadService.uploadFile().success(function (response) {
            if (response.success) {//如果上传成功，取出url
                $scope.image_entity.url = response.message;//设置文件地址
            } else {
                alert(response.message);
            }
        }).error(function () {
            alert("上传发生错误");
        });
    };

    $scope.entity = {goods: {}, tbGoodsDesc: {itemImages: []}};//定义页面实体结构
    //添加图片列表
    $scope.add_image_entity = function () {
        $scope.entity.tbGoodsDesc.itemImages.push($scope.image_entity);
    }

    //列表中移除图片
    $scope.remove_image_entity = function (index) {
        $scope.entity.tbGoodsDesc.itemImages.splice(index, 1);
    }

    //三级导航栏读取内容
    $scope.getItemCatList = function () {
        itemCatService.getItemCatByParentId(0).success(
            function (data) {
                $scope.itemCat1List = data;
                //$scope.itemCat3List = {};
            }
        )
    }

    $scope.$watch("entity.tbGoods.category1Id", function (newValue, oldValue) {

        itemCatService.getItemCatByParentId(newValue).success(
            function (data) {
                $scope.itemCat2List = data;
                $scope.itemCat3List = {};
            }
        )
    })

    $scope.$watch("entity.tbGoods.category2Id", function (newValue, oldValue) {

        itemCatService.getItemCatByParentId(newValue).success(
            function (data) {
                $scope.itemCat3List = data;
            }
        )

    })

    $scope.$watch("entity.tbGoods.category3Id", function (newValue, oldValue) {

        itemCatService.getItemCatById(newValue).success(
            function (data) {
                //alert(data.typeId)
                $scope.entity.tbGoods.typeTemplateId = data.typeId;
            }
        )
    })


    $scope.statusList = ["未申请", "申请中", "审核通过", "已驳回"]
    $scope.$watch("entity.tbGoods.typeTemplateId", function (newValue, oldValue) {

        typeTemplateService.findOne(newValue).success(
            function (data) {
                $scope.typeTemplate = data;//获取类型模板
                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);//品牌列表

                if($location.search()['id']==null){
                    $scope.entity.tbGoodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);//扩展属性
                }

            }
        )

        //查询规格列表
        typeTemplateService.findSpecList(newValue).success(
            function (response) {
                $scope.specList = response;
            }
        );
    })

    $scope.entity = {tbGoodsDesc: {itemImages: [], specificationItems: []}};
    $scope.updateSpecAttribute = function ($event, name, value) {
        var object = $scope.searchObjectByKey(
            $scope.entity.tbGoodsDesc.specificationItems, 'attributeName', name);
        if (object != null) {
            if ($event.target.checked) {
                object.attributeValue.push(value);
            } else {//取消勾选

                object.attributeValue.splice(object.attributeValue.indexOf(value), 1);//移除选项
                //如果选项都取消了，将此条记录移除
                if (object.attributeValue.length == 0) {
                    $scope.entity.tbGoodsDesc.specificationItems.splice(
                        $scope.entity.tbGoodsDesc.specificationItems.indexOf(object), 1);
                }
            }
        } else {
            $scope.entity.tbGoodsDesc.specificationItems.push(
                {"attributeName": name, "attributeValue": [value]});
        }
    }

    //创建SKU列表
    $scope.createItemList = function () {
        $scope.entity.itemList = [{spec: {}, price: 0, num: 99999, status: '0', isDefault: '0'}];//初始
        var items = $scope.entity.tbGoodsDesc.specificationItems;
        for (var i = 0; i < items.length; i++) {
            $scope.entity.itemList = addColumn($scope.entity.itemList, items[i].attributeName, items[i].attributeValue);
        }
    }
    //添加列值
    addColumn = function (list, columnName, conlumnValues) {
        var newList = [];//新的集合
        for (var i = 0; i < list.length; i++) {
            var oldRow = list[i];
            for (var j = 0; j < conlumnValues.length; j++) {
                var newRow = JSON.parse(JSON.stringify(oldRow));//深克隆
                newRow.spec[columnName] = conlumnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
    }

    $scope.itemCatList = [];//商品分类列表
    //加载商品分类列表
    $scope.findItemCatList = function () {
        itemCatService.findAll().success(
            function (response) {
                for (var i = 0; i < response.length; i++) {
                    $scope.itemCatList[response[i].id] = response[i].name;
                }
            }
        );
    }

    //判断规格是否已经选中
    $scope.checkAttributeValue = function (specName,optionName) {
        var items = $scope.entity.tbGoodsDesc.specificationItems;
        var object = $scope.searchObjectByKey(items,'attributeName',specName);
        if(object!=null){
            if (object.attributeValue.indexOf(optionName)>=0){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
});	
