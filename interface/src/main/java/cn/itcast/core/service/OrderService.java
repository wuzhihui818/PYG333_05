package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;

public interface OrderService {
    void add(Order order, String userName);

    PageResult findByPage(String name);
}
