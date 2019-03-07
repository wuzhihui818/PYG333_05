package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {

    @Reference
    private ItemCatService itemCatService;

    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId) {
        List<ItemCat> list = itemCatService.findByParentId(parentId);
        return list;
    }

    @RequestMapping("/findOne")
    public ItemCat findOne(Long id) {
        return itemCatService.findOne(id);
    }

    @RequestMapping("/findAll")
    public List<ItemCat> findAll() {
        return itemCatService.findAll();
    }


    @RequestMapping("/addItemCat")
    public Result addItemCat(@RequestBody ItemCat itemCat){
        itemCat.setStatus("0");
        try {
            itemCatService.addItemCat(itemCat);
            return new Result(true,"申请提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"申请提交失败");
        }
    }
}
