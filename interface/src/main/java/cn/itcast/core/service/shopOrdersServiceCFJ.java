package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.OrderEntity;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import com.alibaba.dubbo.config.annotation.Service;


public interface shopOrdersServiceCFJ {

    /*
     *   陈福健
     *
     * */
    public PageResult findPage(Integer page, Integer rows, Order order);

    /*
     *   陈福健
     *
     * */
    public OrderEntity serach(Order order);

}
