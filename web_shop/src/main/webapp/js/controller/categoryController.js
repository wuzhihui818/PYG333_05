// 定义控制器:
app.controller("categoryController",function($scope,$controller,$http,categoryService){
    // AngularJS中的继承:伪继承
    $controller('baseController',{$scope:$scope});


    // 分页查询
    $scope.findByPage = function(page,rows){
        // 向后台发送请求获取数据:
        categoryService.findByPage(page,rows).success(function(response){
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
            object = categoryService.update($scope.entity);
        }else{
            // 保存
            object = categoryService.save($scope.entity);
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
    // 查询父级分类:
    $scope.findParentId = function(){
        var id=0;
        categoryService.findParentId(id).success(function(response){
            // {id:xx,name:yy,firstChar:zz}
            $scope.parentList = response;
        });
    }


    // 查询一个:
    $scope.findById = function(id){
        categoryService.findById(id).success(function(response){
            // {id:xx,name:yy,firstChar:zz}
            $scope.entity = response;
        });
    }

    // 删除品牌:
    $scope.dele = function(){
        categoryService.dele($scope.selectIds).success(function(response){
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
    /*   /!**!/ $scope.status = ["未审核","审核通过"];*/
    // 假设定义一个查询的实体：searchEntity
    $scope.search = function(page,rows){
        // 向后台发送请求获取数据:
        categoryService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }
    // 显示状态
    $scope.status = ["未审核","审核通过"];
});
