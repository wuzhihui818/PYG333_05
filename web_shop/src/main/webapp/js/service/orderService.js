//服务层
app.service('orderService',function($http){

    //删除
    this.dele=function(ids){
        return $http.get('../order/delete.do?ids='+ids);
    }
    //搜索
    this.search=function(page,rows,searchEntity){
        return $http.post('../order1/search.do?page='+page+"&rows="+rows,searchEntity);
    }


    this.find=function(findEntity){

        console.log(findEntity);
        return $http.post('../order/find.do?1='+"1",findEntity);
    }


    //审核状态
    this.updateStatus = function(ids,status){
        return $http.get('../order1/updateStatus.do?ids='+ids+"&status="+status);
    }

});
