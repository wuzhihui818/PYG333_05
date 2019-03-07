package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;


public interface OrderItemService {

    //品优购用户中心-订单查询
    public PageResult findAll(String userName, Integer page, Integer rows, String status);
}
