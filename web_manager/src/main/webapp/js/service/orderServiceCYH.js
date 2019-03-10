//服务层
app.service('orderServiceCYH',function($http){

    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../orderKill/search.do?page='+page+"&rows="+rows);
    }

    this.updateStatus = function(ids,status){
        return $http.get('../seckill/updateStatus.do?ids='+ids+"&status="+status);
    }
});
