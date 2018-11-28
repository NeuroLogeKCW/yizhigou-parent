app.controller("specificationController", function ($scope, $controller, specificationService) {

    $controller("baseController",{$scope:$scope})//集成基controller的内容

    $scope.searchEntity = {};
    $scope.search = function (page, rows) {
        specificationService.search(page,rows,$scope.searchEntity).success(
            function (response) { //pageResult
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        )
    }

    $scope.entity = {};
    $scope.save = function () {

        var returnObj = null;
        alert($scope.entity.tbSpecification.id)
        if ($scope.entity.tbSpecification.id) {
            returnObj = specificationService.update($scope.entity)
        } else {
            returnObj = specificationService.add($scope.entity)
        }

        returnObj.success(
            function (resp) {

                if (resp.success) {
                    $scope.reloadList();
                } else {
                    alert(resp.message)
                }
            }
        );
    }

    $scope.findOne = function (id) {

        specificationService.findOne(id).success(
            function (resp) {

                $scope.entity = resp;
            }
        )
    }



    $scope.delete = function () {

        specificationService.delete($scope.ids).success(
            function (resp) {
                if (resp.success) {
                    $scope.reloadList();
                }
            }
        )
    }


    //新增选项行
    $scope.addTableRow=function(){
        $scope.entity.tbSpecificationOptionList.push({});
    }

    $scope.delTableRow = function (index) {
        $scope.entity.tbSpecificationOptionList.splice(index,1);
    }
});