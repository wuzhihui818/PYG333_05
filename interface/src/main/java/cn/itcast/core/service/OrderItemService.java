package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.OrderEntity;
import cn.itcast.core.pojo.order.OrderItem;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


public interface OrderItemService {
    //品优购用户中心-订单查询
   /* public PageResult findAll(String userName,Integer page, Integer rows,String status);
*/
   /* //品优购用户中心-订单查询
    public OrderEntity findAll(String userName, String status);*/
    public OrderEntity findAll(String userName);

}
