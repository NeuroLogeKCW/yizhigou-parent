app.controller("brandController", function ($scope, $controller, brandService) {

    $controller("baseController",{$scope:$scope})//集成基controller的内容

    $scope.searchEntity = {};
    $scope.search = function (page, rows) {
        brandService.search(page,rows,$scope.searchEntity).success(
            function (response) { //pageResult
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        )
    }

    $scope.entity = {};
    $scope.save = function () {

        var returnObj = null;
        if ($scope.entity.id) {
            returnObj = brandService.update($scope.entity)
        } else {
            returnObj = brandService.add($scope.entity)
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

        brandService.findOne(id).success(
            function (resp) {

                $scope.entity = resp;
            }
        )
    }



    $scope.delete = function () {

        brandService.delete($scope.ids).success(
            function (resp) {
                if (resp.success) {
                    $scope.reloadList();
                }
            }
        )
    }
});