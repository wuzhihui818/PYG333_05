package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Brand;
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

    /**
     * 高级查询(分页, 高级查询)
     * @param page  当前页
     * @param rows  每页展示多少条数据
     * @param itemCat 需要查询的条件分类对象
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody ItemCat itemCat) {
        PageResult pageResult = itemCatService.findPage(itemCat ,page, rows);
        return pageResult;
    }
    /**
     * 添加
     * @param itemCat  品牌对象
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.add(itemCat);
            return  new Result(true, "添加成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false, "添加失败!");
        }
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
