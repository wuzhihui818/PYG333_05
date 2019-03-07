package cn.itcast.core.listener;


import cn.itcast.core.common.HttpClient;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.log.PayLog;
import cn.itcast.core.pojo.order.Order;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.HashMap;
import java.util.Map;

public class CloseOrderListener implements MessageListener {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderDao orderDao;
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

    @Override
    public void onMessage(Message message) {
        ActiveMQTextMessage textMessage = (ActiveMQTextMessage) message;
        try {
//            获得订单id
            String userName = textMessage.getText();
//            从redis中获取订单支付信息,如果能获取到,则说明一直未支付,可以关闭订单,
//              如果获取不到则说明已经支付,不需要进行任何操作
            PayLog payLog = (PayLog) redisTemplate.boundHashOps(Fields.PAY_LOG_REDIS).get(userName);
            if (payLog != null) {
//           表名订单未支付,已经超过最大时限,可以关闭订单
                Map<String, String> map = new HashMap<>();
                map.put("appid", appid);
                map.put("mch_id", partner);
                map.put("out_trade_no", payLog.getOutTradeNo());
                map.put("nonce_str", WXPayUtil.generateNonceStr());
                String xml = WXPayUtil.generateSignedXml(map, partnerkey);
                HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
                httpClient.setHttps(true);
                httpClient.setXmlParam(xml);
                httpClient.post();
//                获得返回的结果
                String content = httpClient.getContent();
                Map<String, String> stringMap = WXPayUtil.xmlToMap(content);
                String result_code = stringMap.get("result_code");
                if ("SUCCESS".equals(result_code)) {
//                    表名关闭订单成功,修改数据库中
//                    order表和pay_log表中的状态数据,6表示交易关闭
                    String orderList = payLog.getOrderList();
                    String[] orders = orderList.split(",");
                    Order order = new Order();
                    for (String orderId : orders) {
                        order.setOrderId(Long.parseLong(orderId));
                        order.setStatus("6");
                        orderDao.updateByPrimaryKeySelective(order);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
