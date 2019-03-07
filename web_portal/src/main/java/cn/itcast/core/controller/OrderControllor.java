package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.OrderService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
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
     * 接收订单信息,邮寄地址......
     * 从redis中取出购物车,存入订单信息中,清空购物车
     * @param order
     * @return
     */
    @RequestMapping("add")
    public Result add(@RequestBody Order order){
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            if (order!=null){
                orderService.add(order,userName);
                return new Result(true,"订单添加成功");
            }else {
                return new Result(false,"请填写收货地址");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"订单添加失败");
        }
    }

    /**
     * 根据当前商家名查询订单 返回分页信息
     * @return
     */
    @RequestMapping("/findByPage")
    public PageResult findByPage(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        PageResult pageList = orderService.findByPage(name);
        return pageList;
    }

}
