package cn.itcast.core.service;

import cn.itcast.core.common.IdWorker;
import cn.itcast.core.dao.log.PayLogDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.entity.BuyerCart;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PayLogDao payLogDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 接收订单信息,邮寄地址......
     * 从redis中取出购物车,存入订单信息中,清空购物车
     * 分别存入order表,orderItem表,pay_log表
     *
     * @param order1
     * @param userName
     */
    @Override
    public void add(Order order1, String userName) {
//        从redis中取出购物车
        List<BuyerCart> cartList = (List<BuyerCart>) redisTemplate.boundHashOps(Fields.CARTLIST_REDIS).get(userName);
        if (cartList == null || cartList.size() == 0) {
            throw new RuntimeException("购物车里没有商品");
        }
//        将购物车信息存入order对象中
        List<Order> orderList = creatOrder(order1, cartList, userName);
//        遍历orderList,将order写入数据库
        for (Order order : orderList) {
            orderDao.insertSelective(order);
        }
//        将购物车信息存入orderItem对象中,返回orderItemList集合,遍历该集合,将数据写入数据库
        List<OrderItem> orderItemList = creatOrderItem(cartList);
        for (OrderItem orderItem : orderItemList) {
            orderItemDao.insertSelective(orderItem);
        }
//  将订单信息存入支付日志表pay_log中
        PayLog payLog = creatPayLog(orderList, userName);
        payLogDao.insertSelective(payLog);

//       将订单日志信息存入redis中,值为payLog,键为userName,待付款后将其删除
        redisTemplate.boundHashOps(Fields.PAY_LOG_REDIS).put(userName, payLog);
//       将redis中原来的购物车信息清除
        redisTemplate.boundHashOps(Fields.CARTLIST_REDIS).delete(userName);
    }

    @Override
    public List<Order> findAll() {
        return orderDao.selectByExample(null);
    }


    //将订单信息存入payLog对象中
    private PayLog creatPayLog(List<Order> orderList, String userName) {
        PayLog payLog = new PayLog();
        BigDecimal totalFee = new BigDecimal("0");
        String paymentType = null;
        List<String> orderIdList = new ArrayList<>();
        for (Order order : orderList) {
            totalFee = totalFee.add(order.getPayment());
            orderIdList.add(String.valueOf(order.getOrderId()));
            paymentType = order.getPaymentType();
        }
        String out_trade_no = String.valueOf(idWorker.nextId());

        payLog.setCreateTime(new Date());
        payLog.setOrderList(orderIdList.toString().replace("[", "").replace("]", ""));
        payLog.setOutTradeNo(out_trade_no);
        payLog.setTotalFee(totalFee.longValue());
        payLog.setUserId(userName);
        payLog.setPayType(paymentType);
        payLog.setTradeState("0");
        return payLog;
    }

    // 将购物车信息存入order对象中,cartList中每一个cart都是一个独立的订单
    private List<Order> creatOrder(Order order1, List<BuyerCart> cartList, String userName) {
        List<Order> orderList = new ArrayList<>();
        for (BuyerCart cart : cartList) {
            Order order = new Order();
//           获得订单Id
            long orderId = idWorker.nextId();
            order.setOrderId(orderId);
//            获得payMent实付金额
            List<OrderItem> orderItemList = cart.getOrderItemList();
            BigDecimal payMent = new BigDecimal("0");
            for (OrderItem orderItem : orderItemList) {
                payMent = payMent.add(orderItem.getTotalFee());
//                在orderItem中添加订单Id
                orderItem.setOrderId(orderId);
            }
            order.setPayment(payMent);
//            设置状态status=1 未付款
            order.setStatus("1");
//            设置订单创建时间create_time
            order.setCreateTime(new Date());
//            订单更新时间update_time
            order.setUpdateTime(new Date());
//            user_id用户id
            order.setUserId(userName);
//            source_type=2 订单来源为pc端
            order.setSourceType("2");
//            seller_id商家ID
            order.setSellerId(cart.getSellerId());
//            payment_type支付类型
            order.setPaymentType(order1.getPaymentType());
//            receiver_area_name收货人地址
            order.setReceiverAreaName(order1.getReceiverAreaName());
//            receiver_mobile收货人手机
            order.setReceiverMobile(order1.getReceiverMobile());
//            receiver收货人
            order.setReceiver(order1.getReceiver());
            orderList.add(order);
        }
        return orderList;
    }

    //    将cartList中的orderItem对象设置Id后,放入集合中,其中itemId在creatOrder方法中已设置,返回orderItem集合
    private List<OrderItem> creatOrderItem(List<BuyerCart> cartList) {
        List<OrderItem> orderItemList = new ArrayList<OrderItem>();
        if (cartList != null && cartList.size() > 0) {
            for (BuyerCart cart : cartList) {
                List<OrderItem> orderItemList1 = cart.getOrderItemList();
                if (orderItemList1 != null && orderItemList1.size() > 0) {
                    for (OrderItem orderItem : orderItemList1) {
                        orderItem.setId(idWorker.nextId());
                        orderItemList.add(orderItem);
                    }
                }
            }

        }
        return orderItemList;
    }

    @Override
    public PageResult findPage(Integer page, Integer rows, Order order) {
        //创建查询对象
        OrderQuery query = new OrderQuery();
        if (order != null) {
            //创建where条件对象
            OrderQuery.Criteria criteria = query.createCriteria();
            if (order.getOrderId() != null) {
                criteria.andOrderIdEqualTo(order.getOrderId());
            }
            if (order.getStatus() != null && !"".equals(order.getStatus())){
                criteria.andStatusEqualTo(order.getStatus());
            }
            if (order.getUserId() != null && !"".equals(order.getUserId())){
                criteria.andUserIdEqualTo(order.getUserId());
            }
        }
        PageHelper.startPage(page, rows);
        Page<Order> orderList = (Page<Order>) orderDao.selectByExample(query);
        return new PageResult(orderList.getTotal(), orderList.getResult());
    }
}
