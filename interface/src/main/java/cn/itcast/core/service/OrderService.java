package cn.itcast.core.service;


import cn.itcast.core.pojo.order.Order;

import java.util.Map;

public interface OrderService {
    void add(Order order, String userName);

    Map<String,Double> findSaleData(String start, String end, String sellerId);
}
