package cn.itcast.core.service;

import cn.itcast.core.common.HttpClient;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class SeckillOrderPayServiceImpl implements SeckillOrderPayService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ActiveMQQueue close_seckillorder;
    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;
    @Value("${notifyurl}")
    private String notifyurl;
    @Value("${requesturl}")
    private String requesturl;
    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Override
    public Map creatNative(final String userName) {
        final SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps(Fields.SECKILLGOODSORDER_REDIS).get(userName);
//         创建参数,发送到微信支付
        Map xmlMap = creatWXPayXmlMap(String.valueOf(seckillOrder.getId()));
        try {
            String xmlStr = WXPayUtil.generateSignedXml(xmlMap, partnerkey);
            HttpClient httpClient = new HttpClient(requesturl);
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlStr);
            httpClient.post();
//            获得结果 将结果(xml字符串)转换为map集合,从集合中获取支付链接,封装到map集合中,传回页面
            String content = httpClient.getContent();
//            转换为map集合
            Map<String, String> contentMap = WXPayUtil.xmlToMap(content);
            Map<String, String> map = new HashMap<>();
//            获取支付链接
            map.put("code_url", contentMap.get("code_url"));
            map.put("total_fee", String.valueOf(seckillOrder.getMoney()));
            map.put("out_trade_no", String.valueOf(seckillOrder.getId()));
//            发送点对点延时消息,超过一定时间,查询后如果还未支付则关闭订单
            jmsTemplate.send(close_seckillorder, new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                    TextMessage textMessage = session.createTextMessage(userName);
                    textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1000 * 60);
                    return textMessage;
                }
            });
            return map;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();

    }

    /**
     * 查询支付状态
     */
    @Override
    public String queryPayStatus(String userName) throws Exception {
//        从redis中获取pay_log
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps(Fields.SECKILLGOODSORDER_REDIS).get(userName);
//        生成需要传输到微信接口的参数,map集合
        Map xmlMap = creatXmlMap(String.valueOf(seckillOrder.getId()));
        int flag = 0;
        String msg = "";
        HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
        String toXml = WXPayUtil.generateSignedXml(xmlMap, partnerkey);
        client.setHttps(true);
        client.setXmlParam(toXml);
        while (true) {
            client.post();
            String content = client.getContent();
            Map<String, String> contentMap = WXPayUtil.xmlToMap(content);
            String trade_state = contentMap.get("trade_state");
//如果返回trade_state为success,则为支付成功
            if ("SUCCESS".equals(trade_state)) {
//                    将数据库中关于订单的信息更改为已支付,并将redis中的payLog清除
                updateState(seckillOrder);
                redisTemplate.boundHashOps(Fields.SECKILLGOODSORDER_REDIS).delete(userName);
                msg = "SUCCESS";
                break;

            }
            if ("FAIL".equals(contentMap.get("return_code"))) {
                throw new Exception("支付失败");
            }
            Thread.sleep(4000);
            flag++;
            if (flag > 600) {
                msg = "OVERTIME";
                break;
            }
        }
        return msg;

    }

    //修改付款后的状态信息
    private void updateState(SeckillOrder seckillOrder) {
        seckillOrder.setStatus("1");
        seckillOrder.setPayTime(new Date());
//      修改seckillOrder中的状态为1
        seckillOrderDao.updateByPrimaryKeySelective(seckillOrder);
    }



    //创建请求支付发送至微信的参数,格式为map
    private Map creatWXPayXmlMap(String out_trade_no) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", appid);
        map.put("mch_id", partner);
        map.put("device_info", "www.itcast.cn");
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        map.put("body", "品优购");
        map.put("out_trade_no", out_trade_no);
        map.put("total_fee", "1");
        map.put("spbill_create_ip", "127.0.0.1");
        map.put("trade_type", "NATIVE");
        map.put("product_id", out_trade_no);
        map.put("notify_url", notifyurl);

        return map;
    }

    //创建查询支付状态发送至微信的参数,格式为map
    private Map creatXmlMap(String out_trade_no) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", appid);
        map.put("mch_id", partner);
        map.put("out_trade_no", out_trade_no);
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        return map;
    }
}
