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
@RequestMapping("order")
public class OrderControllor {
    @Reference
    private OrderService orderService;


    /**
     * 根据当前商家名查询订单 返回分页信息
     * @return
     */
    @RequestMapping("/findByPage")
    public PageResult findByPage(Integer page,Integer rows,@RequestBody Order order){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if (name!=null) {
            order.setSellerId(name);
        }
        PageResult pageList = orderService.findByPage(page,rows,order);
        return pageList;


    }

}
