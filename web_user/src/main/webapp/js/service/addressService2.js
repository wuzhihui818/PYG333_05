//服务层
app.service('addressService2',function($http){

    this.findProvince = function(parentId){
        return $http.get("../address/findProvince.do?parentId="+parentId);
    }
    this.findCity = function(parentId){
        return $http.get("../address/findCity.do?parentId="+parentId);
    }
    this.findArea = function(parentId){
        return $http.get("../address/findArea.do?parentId="+parentId);
    }

});
