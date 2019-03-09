// 定义服务层:
app.service("userService",function($http){
	this.findAll = function(){
		return $http.get("../user/findAll.do");
	}
    this.count = function(){
        return $http.get("../user/count.do");
    }
	
	this.findByPage = function(page,rows){
		return $http.get("../user/findByPage.do?page="+page+"&rows="+rows);
	}
	
	this.save = function(entity){
		return $http.post("../user/add.do",entity);
	}
	
	this.update=function(entity){
		return $http.post("../user/update.do",entity);
	}
	
	this.findById=function(id){
		return $http.get("../user/findOne.do?id="+id);
	}
	
	this.dele = function(ids){
		return $http.get("../user/delete.do?ids="+ids);
	}
	
	this.search = function(page,rows,searchEntity){
		return $http.post("../user/search.do?page="+page+"&rows="+rows,searchEntity);
	}
	
	this.selectOptionList = function(){
		return $http.get("../user/selectOptionList.do");
	}
    //审核状态
    //修改
    this.updateStatus = function(ids,status){
        return $http.get('../user/updateStatus.do?ids='+ids+"&status="+status);
    }
});