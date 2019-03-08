 //控制层 
app.controller('userController' ,function($scope,$controller,loginService,userService){

    // 显示左上角的名字
    $controller('baseController',{$scope:$scope});

    $scope.showName=function(){
        loginService.showName().success(
            function(response){
                $scope.loginName=response.loginName;
            }
        );
    }

    //findOneByuserName()

    //显示当前用户名
    //回显
    $scope.findOneByuserName = function () {

        userService.findOneByuserName().success(
            function (response) {
                $scope.userList = response;
            }
        );
    }


    //updateUser





    //注册用户
    $scope.reg=function(){

        //比较两次输入的密码是否一致
        if($scope.password!=$scope.entity.password){
            alert("两次输入密码不一致，请重新输入");
            $scope.entity.password="";
            $scope.password="";
            return ;
        }
        //新增
        userService.add($scope.entity,$scope.smscode).success(
            function(response){
                alert(response.message);
            }
        );


    
	//发送验证码
	$scope.sendCode=function(){
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}
		
		userService.sendCode($scope.entity.phone  ).success(
			function(response){
				alert(response.message);
			}
		);		
	}



        /*//个人中心的用户注册*/
        $scope.regis=function(){

            //比较两次输入的密码是否一致
            if($scope.password!=$scope.entity.password){
                alert("两次输入密码不一致，请重新输入");
                $scope.entity.password="";
                $scope.password="";
                return ;
            }
            //新增
            userService.addUser($scope.entity,$scope.smscode).success(

                function(response){
                    alert(response.message);
                    alert(6666);
                }
            );
        }

    }





});
