package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.service.CollectService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.http.HttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("collect")
public class CollectController {
    @Reference
    private CollectService collectService;
    @CrossOrigin(origins = "http://localhost:8086", allowCredentials = "true")
    @RequestMapping("addGoodsToCollectList")
    public Result addGoodsToCollectList(Long itemId){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(name)){
            return new Result(false,"您还未登录,无法加入收藏");
        }else {
            collectService.addGoodsToCollectList(itemId,name);
            return new Result(true,"收藏成功");
        }
    }

    @CrossOrigin(origins = "http://localhost:8083", allowCredentials = "true")
    @RequestMapping("findCollectList")
    public List<Item> findCollectList(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = user.getUsername();
        return collectService.findItemList(username);
    }

}
