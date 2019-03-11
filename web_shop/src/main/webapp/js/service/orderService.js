//服务层
app.service('orderService',function($http){

    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../order/search.do?page='+page+"&rows="+rows,searchEntity);
    }
    //审核状态
    this.updateStatus = function(ids,status){
        return $http.get('../order/updateStatus.do?ids='+ids+"&status="+status);
    }
});
