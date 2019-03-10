package cn.itcast.core.service;
import java.math.BigDecimal;
import java.util.Date;

import cn.itcast.core.common.IdWorker;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.pojo.seckill.SeckillOrderQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillOrderDao seckillOrderDao;
//    @Autowired
//    private ActiveMQQueue addSeckillGoodsToRedis;
//    @Autowired
//    private ActiveMQQueue deleteSeckillGoodsFromRedis;
    @Autowired
    private ActiveMQQueue deleteSeckillOrderFromRedis;
    /**
     * 通过传入的参数,修改秒杀商品的状态值,如果为1,则为审核通过,可以进行秒杀操作
     * 改完状态后将商品Id通过延时消息发送至上架及下架队列(加入redis及移除redis)
     * 使用springTask
     * @param ids
     * @param status
     * @author wuzhihui
     */
    @Override
    public void updateStatus(Long[] ids, final String status) {
        if (ids!=null){
            SeckillGoods seckillGoods = new SeckillGoods();
            seckillGoods.setStatus(status);
            for (final Long id : ids) {
                seckillGoods.setId(id);
                seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
//               发送延时消息,延迟时间为当前开始时间的毫秒值
//                if ("1".equals(status)){
//                    SeckillGoods seckillGoods1 = seckillGoodsDao.selectByPrimaryKey(id);
//                    final long startTime = seckillGoods1.getStartTime().getTime();
//                    final long endTime = seckillGoods1.getEndTime().getTime();
////                    发送上架队列
//                    jmsTemplate.send(addSeckillGoodsToRedis, new MessageCreator() {
//                        @Override
//                        public Message createMessage(Session session) throws JMSException {
//                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
//                            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,startTime-System.currentTimeMillis());
//                            return textMessage;
//                        }
//                    });
////                    发送下架队列
//                    jmsTemplate.send(deleteSeckillGoodsFromRedis, new MessageCreator() {
//                        @Override
//                        public Message createMessage(Session session) throws JMSException {
//                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
//                            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,endTime-System.currentTimeMillis());
//                            return textMessage;
//                        }
//                    });

//                }


            }
        }




    }

    /**
     * 查询秒杀商品集合从redis中
     * @return
     * @author wuzhihui
     */
    @Override
    public List<SeckillGoods> findList() {
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).values();
        if (seckillGoodsList!=null && seckillGoodsList.size()>0){

            return seckillGoodsList;
        }else {
            return new ArrayList<>();
        }
    }

    /**
     * wuzhihui
     * 显示秒杀商品详请
     * @param id
     * @return
     */
    @Override
    public SeckillGoods findOneFromRedis(Long id) {
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).get(id);
        return seckillGoods;
    }

    /**
     *
     * 秒杀订单下单
     * 先从redis中获取订单,判断该商品是否有剩余,如果没有剩余,直接抛出一个异常
     * 如果有剩余,修改取出的商品库存,再次获得redis中的剩余库存,如果与之前相同为有效订单,下单成功,如果不同,下单失败
     * wuzhihui
     * @param seckillId
     */
    @Override
    public void submitOrder(Long seckillId, String name) {
//        从redis中获取秒杀商品
        SeckillGoods seckillGoods1 = (SeckillGoods) redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).get(seckillId);
//        判断剩余库存
        Integer stockCount1 = seckillGoods1.getStockCount();
//        判断是否还有库存
        if (stockCount1>0){
            seckillGoods1.setStockCount(stockCount1-1);
//            再次获得秒杀商品库存
            SeckillGoods seckillGoods2 = (SeckillGoods) redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).get(seckillId);
            if (stockCount1.equals(seckillGoods2.getStockCount())){
//                第一次和第二次获得的剩余数一样,下单成功
//                修改数据库中的秒杀商品剩余库存,再存到redis中
                redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).put(seckillGoods2.getId(),seckillGoods2);
                seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods1);
//              将商品加入到redis中
//                订单id
                final long orderId = new IdWorker().nextId();
                redisTemplate.boundHashOps(Fields.SECKILLGOODSORDER_REDIS).put(orderId,seckillGoods1);
//                将商品添加到数据库秒杀订单表中
                SeckillOrder seckillOrder=creatSeckillOrder(orderId,name,seckillGoods1);
                seckillOrderDao.insertSelective(seckillOrder);

//                 发送点对点消息,五分钟后删除redis中的订单消息
                jmsTemplate.send(deleteSeckillOrderFromRedis, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage message = session.createTextMessage(String.valueOf(orderId));
                        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,1000*60*5);
                        return message;

                    }
                });
            }else {

                throw new RuntimeException("无效操作");
            }
        }else {

            throw new RuntimeException("宝贝已经被抢光了");
        }
    }
//创建seckillorder对象
    private SeckillOrder creatSeckillOrder(long orderId, String name, SeckillGoods seckillGoods1) {
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setId(orderId);
        seckillOrder.setSeckillId(seckillGoods1.getId());
        seckillOrder.setMoney(seckillGoods1.getCostPrice());
        seckillOrder.setUserId(name);
        seckillOrder.setSellerId(seckillGoods1.getSellerId());
        seckillOrder.setCreateTime(new Date());
        seckillOrder.setStatus("0");
        return seckillOrder;
    }

    /**
     * 获得秒杀到的商品订单
     * @param name
     * @return
     */
    @Override
    public SeckillOrder findSeckillOrder(String name) {
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps(Fields.SECKILLGOODSORDER_REDIS).get(name);

        SeckillOrderQuery query = new SeckillOrderQuery();
        SeckillOrderQuery.Criteria criteria = query.createCriteria();
        criteria.andSeckillIdEqualTo(seckillGoods.getId());
        criteria.andStatusEqualTo("0");
        List<SeckillOrder> seckillOrders = seckillOrderDao.selectByExample(query);
        if (seckillOrders!=null && seckillOrders.size()>0){
            SeckillOrder seckillOrder = seckillOrders.get(0);
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            seckillOrder.setSmallPic(seckillGoods.getSmallPic());
            seckillOrder.setTitle(seckillGoods.getTitle());
//            将订单信息存入redis,将原来的覆盖
            redisTemplate.boundHashOps(Fields.SECKILLGOODSORDER_REDIS).put(name,seckillOrder);
            return seckillOrder;
        }
        return null;
    }

    @Override
    public void addOrder(SeckillOrder seckillOrder) {
        seckillOrderDao.updateByPrimaryKeySelective(seckillOrder);
    }
}
