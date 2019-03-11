app.controller('seckilladdController' ,function($scope,$controller,seckillService){

    $controller('baseController',{$scope:$scope});//继承
    //分页查询

    $scope.searchEntity={};
    /*   /!**!/ $scope.status = ["未审核","审核通过"];*/
    // 假设定义一个查询的实体：searchEntity
    $scope.search = function(page,rows){
        // 向后台发送请求获取数据:
        seckillService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }




});