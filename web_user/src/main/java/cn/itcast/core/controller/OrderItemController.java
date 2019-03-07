package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.OrderItemService;
import com.alibaba.dubbo.config.annotation.Reference;
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


    @RequestMapping("/findAll")
    public PageResult findAll(Integer page, Integer rows,@RequestBody String status) {
        //1. 获取当前登录用户的用户名
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        //2. 根据用户名和订单状态查询订单
        PageResult pageResult = orderItemService.findAll(userName,page,rows, status);
        return pageResult;
    }
}
