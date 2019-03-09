package cn.itcast.core.listener;

import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * wuzhihui
 * 删除redis中已秒杀到的订单
 */

public class DeleteSeckillOrderFromRedisListener implements MessageListener {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage= (TextMessage) message;
            redisTemplate.boundHashOps(Fields.SECKILLGOODSORDER_REDIS).delete(textMessage.getText());
            SeckillOrder seckillOrder = new SeckillOrder();
            seckillOrder.setId(Long.parseLong(textMessage.getText()));
//            表示订单已经过期
            seckillOrder.setStatus("2");
            seckillOrderDao.updateByPrimaryKeySelective(seckillOrder);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
