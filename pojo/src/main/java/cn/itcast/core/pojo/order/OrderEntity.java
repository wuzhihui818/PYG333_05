package cn.itcast.core.pojo.order;

import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * 自定义封装订单实体类,里面包含订单对象,订单详请对象
 */
public class OrderEntity implements Serializable {
    //订单对象
    private List<Order> orderList;
    //订单详请对象
    private List<OrderItem> orderItemList;

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
