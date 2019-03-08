package cn.itcast.core.listener;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class AddSeckillGoodsToRedisListener implements MessageListener {
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 接收秒杀上架商品的id,message就是id
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            ActiveMQTextMessage textMessage= (ActiveMQTextMessage) message;
            String id = textMessage.getText();
            SeckillGoods seckillGoods = seckillGoodsDao.selectByPrimaryKey(Long.parseLong(id));
            redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).put(id,seckillGoods);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
