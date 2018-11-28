app.controller("indexController", function ($scope, $controller, loginService) {

    $scope.showLoginName = function () {

        loginService.getLoginName().success(function (data) {

            $scope.loginName = data.loginName;
        });
    }
})