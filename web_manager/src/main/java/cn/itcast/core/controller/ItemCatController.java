package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
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

    @RequestMapping("/findAll")
    public List<ItemCat> findAll() {
        return itemCatService.findAll();
    }
    @RequestMapping("/updateStatus")
    public Result updateStatus (Long [] ids,String status){
        try {
           itemCatService.updateStatus(ids,status);
            return  new Result(true, "修改数据状态成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, "修改数据状态失败!");
        }
    }


}
