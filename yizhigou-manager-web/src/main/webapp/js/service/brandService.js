app.service("brandService", function ($http) {

    this.search = function (page,rows,searchEntity) {
        return $http.post('../brand/search.do?page=' + page + '&rows=' + rows,searchEntity);
    }

    this.findOne = function (id) {
        return $http.post('../brand/findOne.do?id=' + id);
    }

    this.delete = function (ids) {
        return $http.post('../brand/delete.do?ids=' + ids);
    }

    this.add = function (entity) {
        return $http.post("../brand/add.do" , entity);
    }

    this.update = function (entity) {
        return $http.post("../brand/update.do" , entity);
    }

    //请求品牌列表
    this.selectBrandOptionList = function () {
        return $http.post("../typeTemplate/selectBrandOptionList.do");
    }
})