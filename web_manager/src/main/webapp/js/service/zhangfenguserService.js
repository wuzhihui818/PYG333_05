// 定义服务层:
app.service("zhangfenguserService",function($http){
	this.findAll = function(){
		return $http.get("../user1/findAll.do");
	}
    this.count = function(){
        return $http.get("../user1/count.do");
    }
	
	this.findByPage = function(page,rows){
		return $http.get("../user1/findByPage.do?page="+page+"&rows="+rows);
	}
	
	this.save = function(entity){
		return $http.post("../user1/add.do",entity);
	}
	
	this.update=function(entity){
		return $http.post("../user1/update.do",entity);
	}
	
	this.findById=function(id){
		return $http.get("../user1/findOne.do?id="+id);
	}
	
	this.dele = function(ids){
		return $http.get("../user1/delete.do?ids="+ids);
	}
	
	this.search = function(page,rows,searchEntity){
		return $http.post("../user1/search.do?page="+page+"&rows="+rows,searchEntity);
	}
	
	this.selectOptionList = function(){
		return $http.get("../user1/selectOptionList.do");
	}
    //审核状态
    //修改
    this.updateStatus = function(ids,status){
        return $http.get('../user1/updateStatus.do?ids='+ids+"&status="+status);
    }
});