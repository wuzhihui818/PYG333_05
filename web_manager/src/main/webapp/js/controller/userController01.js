 //控制层
app.controller('userController01' ,function($scope,$controller   ,userService01) {
    $controller('baseController', {$scope: $scope});//继承

    // //注册用户
    // $scope.reg=function(){
    //
    // 	//比较两次输入的密码是否一致
    // 	if($scope.password!=$scope.entity.password){
    // 		alert("两次输入密码不一致，请重新输入");
    // 		$scope.entity.password="";
    // 		$scope.password="";
    // 		return ;
    // 	}
    // 	//新增
    // 	userService.add($scope.entity,$scope.smscode).success(
    // 		function(response){
    // 			alert(response.message);
    // 		}
    // 	);
    // }
    //
    // //发送验证码
    // $scope.sendCode=function(){
    // 	if($scope.entity.phone==null || $scope.entity.phone==""){
    // 		alert("请填写手机号码");
    // 		return ;
    // 	}
    //
    // 	userService.sendCode($scope.entity.phone  ).success(
    // 		function(response){
    // 			alert(response.message);
    // 		}
    // 	);
    // }


    $scope.status = ["冻结", "激活"];
    $scope.specKk = function (status) {
        userService01.specKk($scope.selectIds, status).success(function (response) {
            if (response.success) {
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            } else {
                alert(response.message);
            }
        });
    }

// 显示状态

    //搜索
    $scope.searchUser = {};//定义搜索对象
    $scope.search = function (page, rows) {
        userService01.search(page, rows, $scope.searchUser).success(
            function (response) {
                $scope.list = response.rows;
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }


});