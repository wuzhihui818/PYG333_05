package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderEntity;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.OrderItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderItem")

public class OrderItemController {


    @Reference
    private OrderItemService orderItemService;


    /*@RequestMapping("/findAll")
    public PageResult findAll(Integer page, Integer rows, @RequestBody String status) {
        //1. 获取当前登录用户的用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //2. 根据用户名和订单状态查询订单
        PageResult pageResult = orderItemService.findAll(userName, page, rows, status);
        return pageResult;}
*/

    /*@RequestMapping("/findAll")
    public OrderEntity findAll(@RequestBody String status) {
        //1. 获取当前登录用户的用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //2. 根据用户名和订单状态查询订单
        OrderEntity roderEntityList = orderItemService.findAll(userName, status);
        return roderEntityList;
    }*/
   /* @RequestMapping("/findAll")
    public List<OrderItem> findAll() {
        //1. 获取当前登录用户的用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderEntity entity = orderItemService.findAll(userName);

        return entity.getOrderItemList();
    }*/
    @RequestMapping("/findAll")
    public OrderEntity findAll() {
        //1. 获取当前登录用户的用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        OrderEntity entity = orderItemService.findAll(userName);

        return entity;
    }

}
