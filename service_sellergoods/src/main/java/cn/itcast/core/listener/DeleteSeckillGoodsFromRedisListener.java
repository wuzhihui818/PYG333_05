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

public class DeleteSeckillGoodsFromRedisListener implements MessageListener {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    @Override
    public void onMessage(Message message) {
        try {
            ActiveMQTextMessage textMessage= (ActiveMQTextMessage) message;
            String id = textMessage.getText();
            redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).delete(id);
            SeckillGoods seckillGoods = new SeckillGoods();
//            将数据库中秒杀商品的状态改为2,表示已经过期
            seckillGoods.setId(Long.parseLong(id));
            seckillGoods.setStatus("2");
            seckillGoodsDao.updateByPrimaryKeySelective(seckillGoods);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
