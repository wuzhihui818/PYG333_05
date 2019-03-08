package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.pojo.order.OrderItemQuery;
import cn.itcast.core.pojo.order.OrderQuery;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.user.User;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ActiveMQQueue smsDestination;

    @Value("${template_code}")
    private String template_code;
    @Value("${sign_name}")
    private String sign_name;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private SellerDao sellerDao;


    @Override
    public void sendCode(final String phone) {
        //1. 生成随机6位以内的数字作为验证码
        final String code = String.valueOf((long)(Math.random() * 1000000));
        //2. 将手机号作为key, 验证码作为value存入redis中
        redisTemplate.boundValueOps(phone).set(code, 10, TimeUnit.MINUTES);
        //3. 将手机号, 验证码, 签名, 模板编号等信息组成消息对象发送给消息服务器
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = new ActiveMQMapMessage();
                //手机号
                mapMessage.setString("mobile", phone);
                //模板编号
                mapMessage.setString("template_code", template_code);
                //签名
                mapMessage.setString("sign_name", sign_name);

                //验证码
                Map<String, String> codeMap = new HashMap<>();
                codeMap.put("code", code);
                mapMessage.setString("param", JSON.toJSONString(codeMap));
                return mapMessage;
            }
        });
    }

    @Override
    public boolean checkSmsCode(String phone, String smsCode) {
        //1. 判断手机号和验证码不为空
        if (phone == null || "".equals(phone) || smsCode == null || "".equals(smsCode)) {
            return false;
        }
        //2. 根据手机号到redis中获取我们保存的验证码
        String redisSmsCode = (String)redisTemplate.boundValueOps(phone).get();
        //3. 判断使用手机号是否能够取出验证码
        if (redisSmsCode == null || "".equals(redisSmsCode)) {
            return false;
        }
        //4. 判断我们的验证码和页面传入的验证码是否一致
        if (!smsCode.equals(redisSmsCode)) {
            return false;
        }
        return true;
    }

    @Override
    public void add(User user) {
        userDao.insertSelective(user);
    }


    /*
    新增个人信息
    * */
    @Override
    public void addUser(String userName,User user) {
        User user1 = new User();
        user1.setUsername(userName);
        userDao.updateByPrimaryKey(user);
    }

    @Override
    public PageResult search(String userName, Integer page, Integer rows, Order order) {

        //设置分页条件
        PageHelper.startPage(page, rows);
        //根据登录用户查询所有订单
        //根据登录用户id,查询所有订单信息
        OrderQuery orderQuery = new OrderQuery();
        OrderQuery.Criteria criteria = orderQuery.createCriteria();
        criteria.andUserIdEqualTo(userName);
        if(null != order && StringUtils.isNotBlank(order.getStatus())){
            criteria.andStatusEqualTo(order.getStatus());
        }

        //返回分页查询后的订单 格式
        Page<Order> orderList = (Page<Order>) orderDao.selectByExample(orderQuery);

        if (orderList != null && orderList.size() > 0) {
            for (Order order1 : orderList) {


                //获取订单 id
                Long orderId = order1.getOrderId();
                //根据订单id,查询订单 详情orderItem
                OrderItemQuery orderItemQuery = new OrderItemQuery();
                OrderItemQuery.Criteria orderItemQueryCriteria = orderItemQuery.createCriteria();
                orderItemQueryCriteria.andOrderIdEqualTo(orderId);
                List<OrderItem> orderItemList = orderItemDao.selectByExample(orderItemQuery);

              if (orderItemList != null && orderItemList.size() > 0) {
                    for (OrderItem orderItem : orderItemList) {
                        //根据orderItem里面的itemid获取item对象
                        Long itemId = orderItem.getItemId();
                        Item item = itemDao.selectByPrimaryKey(itemId);
                        orderItem.setSpellMap(item.getSpecMap());
                        orderItem.setCostPirce(item.getCostPirce());
                        orderItem.setMarketPrice(item.getMarketPrice());

                    }
                }

                //获取order里面的seller_id,
                String sellerId = order1.getSellerId();
                //根据商家id,查询商家名称
                Seller seller = sellerDao.selectByPrimaryKey(sellerId);
                String sellerNickName = seller.getNickName();

                order1.setSellerNickName(sellerNickName);
                order1.setOrderItemList(orderItemList);


            }
        }

        //将结果封装到pageResult中返回
        PageResult pageResult = new PageResult(orderList.getTotal(), orderList.getResult());
        return pageResult;
    }



    @Override
    public List<Order> findAll(String userName) {
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.createCriteria().andUserIdEqualTo(userName);
        List<Order> orders = orderDao.selectByExample(orderQuery);
        return orders;
    }




}
