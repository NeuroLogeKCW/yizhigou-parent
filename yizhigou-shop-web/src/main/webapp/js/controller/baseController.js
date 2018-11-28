app.controller('baseController' ,function($scope) {
    $scope.reloadList = function () {
        //切换页码
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }
//分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 5,
        perPageOptions: [5, 10, 20, 30, 40],
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };

    /**
     * 获取选中的选项的id值
     * @type {Array}
     */
    $scope.ids = [];
    $scope.updateSelection = function ($event, id) {

        if ($event.target.checked) {//选中则加上这个id

            $scope.ids.push(id);
        } else {//否则去掉

            var index = $scope.ids.indexOf(id);
            $scope.ids.splice(index, 1);//从index开始删除1个
        }
        console.log($scope.ids);
    }

    //从集合中按照key查询对象
    $scope.searchObjectByKey=function(list,key,keyValue){
        for(var i=0;i<list.length;i++){
            if(list[i][key]==keyValue){
                return list[i];
            }
        }
        return null;
    }
})