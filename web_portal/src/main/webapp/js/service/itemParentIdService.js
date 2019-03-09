//服务层
app.service('itemParentIdService', function ($http) {

    //查询商品分类信息
    this.findByParentId = function(parentId){
        return $http.get("../itemCat/findByParentId.do?parentId="+parentId);
    }
});
