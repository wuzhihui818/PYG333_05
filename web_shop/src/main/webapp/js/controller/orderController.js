 //控制层 
app.controller('orderController' ,function($scope,$controller,orderService){
	
	$controller('baseController',{$scope:$scope});//继承
	


	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
        orderService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds = [];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){
        orderService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    

});	
