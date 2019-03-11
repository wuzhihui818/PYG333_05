package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/findAll")
    public List<Order> findAll(){
        return orderService.findAll();
    }

    /**
     * 高级查询(分页, 高级查询)
     * @param page  当前页
     * @param rows  每页展示多少条数据
     * @param order 需要查询的条件品牌对象
     * @return*/

    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows,@RequestBody Order order) {
        PageResult pageResult = orderService.findPage(page,rows,order);
        return pageResult;
    }

    @RequestMapping("/findPage")
    public PageResult findPage(Integer page, Integer rows){
        PageResult pageResult = orderService.findPage(page, rows,null);
        return pageResult;
    }
}
