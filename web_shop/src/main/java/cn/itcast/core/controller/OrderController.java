package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("order1")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody Order order) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if (name != null) {
            order.setSellerId(name);
            return orderService.search(page, rows, order);

        }
        return null;

    }

    //发货未发货 数据修改 3、未发货，4、已发货
    @RequestMapping("updateStatus")
    public Result updateStatus(Long[] ids, String status) {
        try {
            orderService.updateStatus(ids, status);
            return new Result(true, "成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "数据失败");

        }

    }
}