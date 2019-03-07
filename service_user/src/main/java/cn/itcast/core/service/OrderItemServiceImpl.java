package cn.itcast.core.service;


import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {


    @Autowired
    private Order order;
    @Autowired
    private OrderItemDao orderItemDao;




    @Override
    public PageResult findAll(String userName, Integer page, Integer rows, String status) {
        //根据用户名去Order中获取

        return null;
    }
}
