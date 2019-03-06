package cn.itcast.core.service;

import java.math.BigDecimal;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.entity.BuyerCart;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ItemDao itemDao;
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 将商品加入购物车集合
     *
     * @param cartList 购物车集合
     * @param itemId   商品库存id
     * @param num      商品数量
     * @return
     */
    @Override
    public List<BuyerCart> addItemToBuyerCartList(List<BuyerCart> cartList, Long itemId, Integer num) {
        Item item = itemDao.selectByPrimaryKey(itemId);
//        if (num < 1) {
//            throw new RuntimeException("商品数量不合法");
//        }
        //        查询是否存在此商品
        if (item == null) {
            throw new RuntimeException("此商品不存在");
        }
//        查询商品库存的status是否为1
        if (!"1".equals(item.getStatus())) {
            throw new RuntimeException("此商品非法");
        }
//        判断原有购物车集合中是否有该商户,如果没有,新建一个,如果有,增加数量
        BuyerCart cart = sellerIsOrNotPartOfCartList(cartList, item);
        if (cart == null) {
//            新建一个cart对象,存入购物车集合中
            cart = new BuyerCart();
            cart.setSellerId(item.getSellerId());
            cart.setSellerName(item.getSeller());
//创建orderItem集合
            List orderItemList = new ArrayList<OrderItem>();
//创建orderItem对象并赋值
            OrderItem orderItem = creatOrderItem(item, num);
//将orderItem对象放入集合
            orderItemList.add(orderItem);
//            将集合放入购物车对象中
            cart.setOrderItemList(orderItemList);
            if (cartList != null) {
                cartList.add(cart);
            }else{
                cartList=new ArrayList<BuyerCart>();
                cartList.add(cart);
            }
        } else {
//            购物车集合中存在此商户的购物车
//            通过判断itemId是否有相同,如果有则包含此商品,只需要增加数量及总价,如果不包含,则添加
            OrderItem orderItem = orderItemIsOrNotPartOfOrderItemList(cart, item);
            if (orderItem == null) {
//                不存在此商品
                orderItem = creatOrderItem(item, num);
                cart.getOrderItemList().add(orderItem);
            } else {
//存在此商品
                orderItem.setNum(orderItem.getNum() + num);
                orderItem.setTotalFee(orderItem.getPrice().multiply(new BigDecimal(orderItem.getNum())));
            }
        }

        return cartList;
    }

    //判断购物车集合中是否有该商家,返回cart代表有,null代表没有
    private BuyerCart sellerIsOrNotPartOfCartList(List<BuyerCart> cartList, Item item) {
        if (cartList != null) {
            for (BuyerCart cart : cartList) {
                if (cart.getSellerId().equals(item.getSellerId())) {
                    return cart;
                }
            }

        }
        return null;
    }

    //    判断orderItem中是否包含同样的商品,返回orderIte带表有,null代表没有
    private OrderItem orderItemIsOrNotPartOfOrderItemList(BuyerCart cart, Item item) {
        List<OrderItem> orderItemList = cart.getOrderItemList();
        if (orderItemList != null) {
            for (OrderItem orderItem : orderItemList) {
                if (item.getId().equals(orderItem.getItemId())) {
                    return orderItem;
                }
            }
        }
        return null;
    }

    //创建orderItem对象.并赋值
    private OrderItem creatOrderItem(Item item, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItemId(item.getId());
        orderItem.setGoodsId(item.getGoodsId());
        orderItem.setTitle(item.getTitle());
        orderItem.setPrice(new BigDecimal(String.valueOf(item.getPrice())));
        orderItem.setNum(num);
        orderItem.setTotalFee(orderItem.getPrice().multiply(new BigDecimal(num)));
        orderItem.setPicPath(item.getImage());
        orderItem.setSellerId(item.getSellerId());
        return orderItem;
    }

    /**
     * 用户登录后将从cookie和redis中取出的购物车列表合并到一起
     *
     * @param cartListJsonStrFromCookie 从cookie中取出的购物车列表json字符串
     * @param cartListFromRedis         从redis中取出的已有的购物车集合
     * @return
     */
    @Override
    public List<BuyerCart> mergeBuyerCartListFromCookieAndRedis(String cartListJsonStrFromCookie, List<BuyerCart> cartListFromRedis) {
        //        对两个集合进行合并操作
        if (cartListJsonStrFromCookie != null && !"".equals(cartListJsonStrFromCookie)) {
            List<BuyerCart> cartList = JSON.parseArray(cartListJsonStrFromCookie, BuyerCart.class);
            if (cartListFromRedis==null){
                cartListFromRedis=new ArrayList<BuyerCart>();
            }
            for (BuyerCart cart : cartList) {
                List<OrderItem> orderItemList = cart.getOrderItemList();
                if (orderItemList != null) {
                    for (OrderItem orderItem : orderItemList) {
                        cartListFromRedis= addItemToBuyerCartList(cartListFromRedis, orderItem.getItemId(), orderItem.getNum());
                    }
                }

            }
        }
        return cartListFromRedis;
    }

    /**
     * 保存cartList到redis
     *
     * @param cartList
     */
    @Override
    public void saveBuyerCartListToRedis(List<BuyerCart> cartList, String userName) {
        redisTemplate.boundHashOps(Fields.CARTLIST_REDIS).put(userName, cartList);
    }
    /**
     * 取出购物车集合from redis
     *
     * @param userName
     * @return
     */
    @Override
    public List<BuyerCart> getBuyerCartListFromRedis(String userName) {
        List<BuyerCart> cartList = (List<BuyerCart>) redisTemplate.boundHashOps(Fields.CARTLIST_REDIS).get(userName);
        return cartList;
    }
}
