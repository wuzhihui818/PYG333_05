package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface OrderService {
    void add(Order order, String userName);
     public PageResult search(Integer page, Integer rows, Order order );
     public void updateStatus(Long [] ids,String status);
}
