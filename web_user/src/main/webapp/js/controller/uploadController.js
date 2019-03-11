app.controller("uploadController",function($scope,uploadService){
    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.success){
                // 获得url
                alert(11111);
                alert(response.message);
                alert(2222);
                $scope.image_entity.url =  response.message;
            }else{
                alert(3333);
                alert(response.message);
            }
        });
    }
});