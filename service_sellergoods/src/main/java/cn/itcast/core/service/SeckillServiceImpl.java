package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.seckill.SeckillGoods;
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
    private ActiveMQQueue addSeckillGoodsToRedis;
    @Autowired
    private ActiveMQQueue deleteSeckillGoodsFromRedis;
    /**
     * 通过传入的参数,修改秒杀商品的状态值,如果为1,则为审核通过,可以进行秒杀操作
     * 改完状态后将商品Id通过延时消息发送至上架及下架队列(加入redis及移除redis)
     * @param ids
     * @param status
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
                if ("1".equals(status)){
                    SeckillGoods seckillGoods1 = seckillGoodsDao.selectByPrimaryKey(id);
                    final long startTime = seckillGoods1.getStartTime().getTime();
                    final long endTime = seckillGoods1.getEndTime().getTime();
//                    发送上架队列
                    jmsTemplate.send(addSeckillGoodsToRedis, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,startTime-System.currentTimeMillis());
                            return textMessage;
                        }
                    });
//                    发送下架队列
                    jmsTemplate.send(deleteSeckillGoodsFromRedis, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,endTime-System.currentTimeMillis());
                            return textMessage;
                        }
                    });

                }


            }
        }




    }

    /**
     * 查询秒杀商品集合从redis中
     * @return
     */
    @Override
    public List<SeckillGoods> findList() {
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).values();
        return seckillGoodsList;
    }
}
