 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
            $scope.entity.parentId=$scope.parentId;//赋予上级ID
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
                    $scope.getItemCatByParentId($scope.parentId);//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.ids ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.ids=null;
				}else{
					alert(response.message)
				}
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    /**
	 * 上级父ID
     * @type {number}
     */
    $scope.parentId=0

	$scope.getItemCatByParentId = function (parentId) {
		$scope.parentId=parentId;
		itemCatService.getItemCatByParentId(parentId).success(
			function (data) {
				$scope.list=data;
            }
		)
    }

    $scope.state;
	$scope.setState = function (state) {
		$scope.state = state
    }


	$scope.prevList = [null,null,null];
	$scope.setPrevList = function (entityPrev) {
		//alert($scope.state)
		if ($scope.state==0){
            $scope.prevList = [null,null,null];
		}
        if ($scope.state==1){
            $scope.prevList = [null,entityPrev,null];
        }
        if ($scope.state==2){
            $scope.prevList[2] = entityPrev;
        }
    }

    $scope.AllTypeTemplates=[];
    $scope.findAllTypeTemplates = function () {
		typeTemplateService.findAll().success(
			function (data) {
                $scope.AllTypeTemplates=data;
            }
		)
    }
    
});	
