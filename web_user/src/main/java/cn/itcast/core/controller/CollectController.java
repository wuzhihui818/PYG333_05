package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.service.CollectService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("collect")
public class CollectController {
    @Reference
    private CollectService collectService;

    /**
     * 将商品添加到我的收藏
     */
//    @CrossOrigin(origins = "http://localhost:8086", allowCredentials = "true")
//    @RequestMapping("addGoodsToCollectList")
//    public Result addGoodsToCollectList(Long itemId) {
////        String name = SecurityContextHolder.getContext().getAuthentication().getName();
////        if ("anonymousUser".equals(name)){
////            return new Result(false,"您还未登录,无法加入收藏");
////        }else {
////            collectService.addGoodsToCollectList(itemId,name);
////            return new Result(true,"收藏成功");
////        }
//        try {
//            collectService.addGoodsToCollectList(itemId);
//            return new Result(true,"收藏成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result(false,"收藏失败");
//        }
//    }

    /**
     * 根据用户名获取收藏列表,再根据收藏列表中的itemId查找商品的库存详请,返回商品的库存集合
     *
     * @return
     */
    @RequestMapping("findCollectList")
    public List<Item> findCollectList() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return collectService.findItemList(name);
    }
}
