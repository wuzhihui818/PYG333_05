package cn.itcast.core.controller;

import cn.itcast.core.pojo.collect.Collect;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.service.CollectService;
import com.alibaba.dubbo.config.annotation.Reference;
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
//            根据用户名查询已经收藏的itemId集合,判断是否已经收藏此商品,已经收藏提示已经收藏,未收藏则进行收藏
            List<Item> itemList = collectService.findItemList(name);
            for (Item item : itemList) {
                if (item.getId().equals(itemId)){
                    return new Result(false,"请不要重复收藏");
                }
            }
            collectService.addGoodsToCollectList(itemId,name);
            return new Result(true,"收藏成功");
        }
    }

//    @CrossOrigin(origins = "http://localhost:8083", allowCredentials = "true")
//    @RequestMapping("findCollectList")
//    public List<Item> findCollectList(){
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
////        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        String username = user.getUsername();
//        return collectService.findItemList(username);
//    }

    /**
     *
     * @param itemId
     * @return
     */
    @RequestMapping("addGoodsToCollect")
    public Result addGoodsToCollect (Long itemId){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            collectService.addGoodsToCollect(itemId,userName);
            return new Result(true,"添加成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败！");
        }
    }

}
