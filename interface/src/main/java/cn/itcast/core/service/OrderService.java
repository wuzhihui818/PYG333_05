package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import com.github.pagehelper.Page;

import java.util.List;

public interface OrderService {
    void add(Order order, String userName);

    public List<Order> findAll();

    public PageResult findPage(Integer page, Integer rows, Order order);

}
