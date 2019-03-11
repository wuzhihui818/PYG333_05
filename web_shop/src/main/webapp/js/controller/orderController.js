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
    $scope.findEntity={};//定义搜索对象
    //显示支付类型
    $scope.payment=["1","在线支付","货到付款"];

    $('#test10').change(function () {
        $scope.findEntity.longtime = $('#test10').val();
    });
    //搜索
    // $scope.search=function(page,rows){
    //
    //     orderService.search(page,rows,$scope.searchEntity).success(
    //         function(response){
    //             $scope.list=response.rows;
    //             $scope.paginationConf.totalItems=response.total;//更新总记录数
    //         }
    //     );
    // }


    $scope.searchEntity={};//定义搜索对象
    //显示支付类型
    $scope.payment=["1","在线支付","货到付款"];
    //搜索
    $scope.search=function(page,rows){
        orderService.search(page,rows,$scope.searchEntity).success(
            function(response){

                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }


    $scope.find=function () {
        orderService.find($scope.findEntity).success(
            function(response) {
                $scope.findEntity = response;
                // $scope.list=response.serach;
                //$scope.searchEntity=response.searchEntity;
                // $scope.paginationConf.totalItems=response.total;
            })};
    // 显示状态
    $scope.status = ["1","未付","已付款","已发货","未发货","交易成功","交易关闭","待评价"];
    // 审核发货的方法:
    $scope.updateStatus = function(status){
        orderService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.success){
                $scope.reloadList();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }

    //保存 数据
    $scope.save=function(){
        var serviceObject;//服务层对象
        if($scope.entity.specification.id!=null){//如果有ID
            serviceObject=seckillService.update( $scope.entity ); //修改
        }else{
            serviceObject=seckillService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    //重新查询
                    $scope.reloadList();//重新加载
                }else{
                    alert(response.message);
                }

            }
        );
    }

});
