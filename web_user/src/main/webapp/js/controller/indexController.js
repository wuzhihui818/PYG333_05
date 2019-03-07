//首页控制器
app.controller('indexController',function($scope,loginService){


    //读取列表数据绑定到表单中
    $scope.findAll=function(){

        loginService.findAll().success(
            function(response){

                $scope.list=response;
            }
        );
    }





    $scope.showName=function(){
			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;
					}
			);
	}
});