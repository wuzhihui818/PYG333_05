//服务层
app.service('orderService',function($http){

	//删除
	this.dele=function(ids){
		return $http.get('../order/delete.do?ids='+ids);
	}
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../order/findByPage.do?page='+page+"&rows="+rows,searchEntity);
	}    	
});
