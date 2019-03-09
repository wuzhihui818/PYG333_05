// 定义服务层:
app.service("categoryService",function($http){
    this.findAll = function(){
        return $http.get("../itemCat/findAll.do");
    }

    this.findByPage = function(page,rows){
        return $http.get("../itemCat/findByPage.do?page="+page+"&rows="+rows);
    }

    this.save = function(entity){
        return $http.post("../itemCat/add.do",entity);
    }

    this.update=function(entity){
        return $http.post("../itemCat/update.do",entity);
    }

    this.findById=function(id){
        return $http.get("../itemCat/findOne.do?id="+id);
    }
    this.findParentId=function(id){
        return $http.get("../itemCat/findByParentId.do?parentId="+id);
    }

    this.dele = function(ids){
        return $http.get("../itemCat/delete.do?ids="+ids);
    }

    this.search = function(page,rows,searchEntity){
        return $http.post("../itemCat/search.do?page="+page+"&rows="+rows,searchEntity);
    }

    this.selectOptionList = function(){
        return $http.get("../itemCat/selectOptionList.do");
    }
});