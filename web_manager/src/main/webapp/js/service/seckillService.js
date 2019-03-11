app.service('seckillService',function($http){
    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../seckill/search.do?page='+page+"&rows="+rows,searchEntity);
    }
    //增加

});