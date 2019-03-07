package cn.itcast.core.service;


import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.*;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {


    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;

    @Override
    public OrderEntity findAll(String userName) {

        OrderEntity orderEntity = new OrderEntity();
        //创建订单条件查询
        OrderQuery query = new OrderQuery();
        OrderQuery.Criteria criteria = query.createCriteria();
        criteria.andUserIdEqualTo(userName);
        List<Order> orderList = orderDao.selectByExample(query);
        orderEntity.setOrderList(orderList);
        //根据订单对象的id作为外键去订单详情的对象中查询
        List<OrderItem> orderItemList =null;
        if (orderList!=null){
            for (Order order : orderList) {
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria criteria1 = orderItemQuery.createCriteria();
                criteria1.andOrderIdEqualTo(order.getOrderId());
                orderItemList = orderItemDao.selectByExample(orderItemQuery);
            }
        }
        //将订单详情对象添加到orderEntity中
        orderEntity.setOrderItemList(orderItemList);

        return orderEntity;

    }

   /* @Override
    public PageResult findAll(String userName, Integer page, Integer rows, String status) {
        //分页查询
        PageHelper.startPage(rows,page);
        //根据用户名和状态 获取
        OrderEntity orderEntity = new OrderEntity();
        OrderQuery query = new OrderQuery();
        OrderQuery.Criteria criteria = query.createCriteria();
        //创建条件查询    userid为name   状态为1 3 4 7
        criteria.andUserIdEqualTo(userName);
        if("1".equals(status)){
            criteria.andStatusEqualTo("1");
        }
        if("3".equals(status)){
            criteria.andStatusEqualTo("3");
        }
        if("4".equals(status)){
            criteria.andStatusEqualTo("4");
        }
        if("7".equals(status)){
            criteria.andStatusEqualTo("7");
        }
        List<Order> ordersList = orderDao.selectByExample(query);
        //将订单对象集合存入到用户订单实体类中
        orderEntity.setOrderList(ordersList);

        //根据订单对象集合,遍历出来,根据order 中的order_id  作为orderItem中的外键查询


        List<OrderItem> orderItemList =null;
        if (ordersList!=null){
            for (Order order : ordersList) {
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria criteria1 = orderItemQuery.createCriteria();
                criteria1.andOrderIdEqualTo(order.getOrderId());
                orderItemList = orderItemDao.selectByExample(orderItemQuery);
            }
        }
        orderEntity.setOrderItemList(orderItemList);
        //TODO 返回结果的时候要对分页结果进行封装, 不知道这么封装

        return null;

    }*/




    /*@Override
    public OrderEntity findAll(String userName, String status) {


        //根据用户名和状态 获取
        OrderEntity orderEntity = new OrderEntity();
        OrderQuery query = new OrderQuery();
        OrderQuery.Criteria criteria = query.createCriteria();
        //创建条件查询    userid为name   状态为1 3 4 7
        criteria.andUserIdEqualTo(userName);
        if("1".equals(status)){
            criteria.andStatusEqualTo("1");
        }
        if("3".equals(status)){
            criteria.andStatusEqualTo("3");
        }
        if("4".equals(status)){
            criteria.andStatusEqualTo("4");
        }
        if("7".equals(status)){
            criteria.andStatusEqualTo("7");
        }
        List<Order> ordersList = orderDao.selectByExample(query);
        //将订单对象集合存入到用户订单实体类中
        orderEntity.setOrderList(ordersList);

        //根据订单对象集合,遍历出来,根据order 中的order_id  作为orderItem中的外键查询


        List<OrderItem> orderItemList =null;
        if (ordersList!=null){
            for (Order order : ordersList) {
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria criteria1 = orderItemQuery.createCriteria();
                criteria1.andOrderIdEqualTo(order.getOrderId());
                orderItemList = orderItemDao.selectByExample(orderItemQuery);
            }
        }
        orderEntity.setOrderItemList(orderItemList);
        return orderEntity;
    }*/

}
