// 定义控制器:
app.controller("userController",function($scope,$controller,$http,userService){
	// AngularJS中的继承:伪继承
	$controller('baseController',{$scope:$scope});
	
	// 查询所有的品牌列表的方法:
	$scope.findAll = function(){
		// 向后台发送请求:
		userService.findAll().success(function(response){
			$scope.list = response;
		});
	}


	// 分页查询
	$scope.findByPage = function(page,rows){
		// 向后台发送请求获取数据:
        userService.findByPage(page,rows).success(function(response){
			$scope.paginationConf.totalItems = response.total;
			$scope.list = response.rows;
		});
	}
	
	// 保存品牌的方法:
	$scope.save = function(){
		// 区分是保存还是修改
		var object;
		if($scope.entity.id != null){
			// 更新
			object = userService.update($scope.entity);
		}else{
			// 保存
			object = userService.save($scope.entity);
		}
		object.success(function(response){
			// {success:true,message:xxx}
			// 判断保存是否成功:
			if(response.success==true){
				// 保存成功
				alert(response.message);
				$scope.reloadList();
			}else{
				// 保存失败
				alert(response.message);
			}
		});
	}
	
	// 查询一个:
	$scope.findById = function(id){
        userService.findById(id).success(function(response){
			// {id:xx,name:yy,firstChar:zz}
			$scope.entity = response;
		});
	}
	
	// 删除品牌:
	$scope.dele = function(){
        userService.dele($scope.selectIds).success(function(response){
			// 判断保存是否成功:
			if(response.success==true){
				// 保存成功
				// alert(response.message);
				$scope.reloadList();
				$scope.selectIds = [];
			}else{
				// 保存失败
				alert(response.message);
			}
		});
	}
	
	$scope.searchEntity={};
	
	// 假设定义一个查询的实体：searchEntity
	$scope.search = function(page,rows){
		// 向后台发送请求获取数据:
        userService.search(page,rows,$scope.searchEntity).success(function(response){
			$scope.paginationConf.totalItems = response.total;
			$scope.list = response.rows;
		});
	}



	//品牌审核
    $scope.updateStatus = function(status){
        userService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.success){
                //重新查询
                $scope.reloadList();//重新加载
				$scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }


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
