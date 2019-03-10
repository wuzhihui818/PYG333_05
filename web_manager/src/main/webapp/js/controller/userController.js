 //控制层
app.controller('userController' ,function($scope,$controller   ,userService){
    $controller('baseController',{$scope:$scope});//继承

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
	}

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

    $scope.searchUser={};//定义搜索对象
    $scope.status = ["冻结","激活"];
    $scope.specKk = function(status){
        userService.specKk($scope.selectIds,status).success(function(response){
            if(response.success){
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }

<<<<<<< HEAD

    $scope.sta1 = function(){
        // 向后台发送请求:
        userService.sta1().success(function(response){
            $scope.sta = response;
        });
    }
    $scope.sta2 = function(){
        // 向后台发送请求:
        userService.sta2().success(function(response){
            $scope.sta2 = response;
        });
    }




    // 显示状态
    $scope.status = ["冻结","非冻结"];
// 显示状态


    $scope.itemCatList = [];
    // 显示分类:
    $scope.findItemCatList = function(){
        userService.findAll().success(function(response){
            //遍历分类表所有数据
            for(var i=0;i<response.length;i++){
                //response[i]获取分类数据某一条
                //response[i].id获取分类数据某一条的id属性值
                //$scope.itemCatList[4]=网路原创
                //$scope.itemCatList[1]=图书、音像、电子书刊
                //itemCatList[分类id]=分类名称
                //{{itemCatList[entity.category1Id]}}相当于itemCatList[分类id]通过分类索引号直接取分类的名称
                $scope.itemCatList[response[i].id] = response[i].name;
            }
        });
    }
});
=======
    //搜索
    $scope.search1=function(page,rows){
        userService.search1(page,rows,$scope.searchUser).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }
	
});	
>>>>>>> remotes/origin/xuhuayu
