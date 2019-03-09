app.controller('addressController',function($scope,$http){
    $scope.shifoumoren=['','默认地址'];
    $scope.findAll=function () {
        $http.get('../address/findListByLoginUser.do').success(
            function(response){
                $scope.addressList=response;
            }
        );
    }

    $scope.findOne=function (id) {
        /*alert("进入findOne")*/
        $http.get('../address/findOne.do?id='+id).success(
            function(response){
                $scope.entity=response;
            }
        );
    }

    //删除地址
    $scope.del=function (id) {
        alert("您确认要删除该地址吗?")
        $http.get('../address/delAddress.do?id='+id).success(
            function(response){
                alert(response.message);
                location.reload();  //实现页面重新加载
            }
        );
    }

    $scope.save=function () {
        /*alert('进入save')*/
        if ($scope.entity.id==null) {
            $http.post('../address/addAddress.do?', $scope.entity).success(
                function (response) {
                    alert(response.message);
                    location.reload();  //实现页面重新加载
                }
            );
        }else{
            $http.post('../address/updateAddress.do?', $scope.entity).success(
                function (response) {
                    alert(response.message);
                    location.reload();  //实现页面重新加载
                }
            );
        }
    }

    //设置默认地址
    $scope.moren=function (id) {
        $http.get('../address/morenAddress.do?id='+id).success(
            function(response){
                alert(response.message);
                location.reload();  //实现页面重新加载
            }
        );
    }

});