app.service("specificationService", function ($http) {

    this.search = function (page,rows,searchEntity) {
        return $http.post('../specification/search.do?page=' + page + '&rows=' + rows,searchEntity);
    }

    this.findOne = function (id) {
        return $http.post('../specification/findOne.do?id=' + id);
    }

    this.delete = function (ids) {
        return $http.post('../specification/delete.do?ids=' + ids);
    }

    this.add = function (entity) {
        return $http.post("../specification/add.do" , entity);
    }

    this.update = function (entity) {
        return $http.post("../specification/update.do" , entity);
    }

    this.selectSpecificationOptionList = function () {
        return $http.get("../typeTemplate/selectSpecificationOptionList.do");
    }
})