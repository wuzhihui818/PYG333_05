package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.OrderEntity;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.shopOrdersServiceCFJ;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderControllerCFJ {

    @Reference
    private shopOrdersServiceCFJ shopOrdersServiceCFJ;

    /**
     * 后端订单分页显示  陈福健
     * @param page  当前页码
     * @param rows  显示数量
     * @param order
     * @return 返回分页订单数据
     */
    @RequestMapping("search")
    public PageResult search(Integer page, Integer rows, @RequestBody Order order) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        order.setSellerId(userName);
        return null;

        /*return shopOrdersServiceCFJ.findPage(page, rows, order);*/
    }

/*
*   陈福健
*
* */
    @RequestMapping("/find")
    public OrderEntity serach(@RequestBody Order order) {
        //获取当前用户用户id  seller id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        String longtime = order.getLongtime();
        String[] times = longtime.split(" - ");
        order.setSellerId(sellerId);
        order.setTimeBegin(times[0]);
        order.setTimeEnd(times[1]);

        OrderEntity orderEntity = shopOrdersServiceCFJ.serach(order);

        return orderEntity;
    }
}
