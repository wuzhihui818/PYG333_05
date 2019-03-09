package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.BuyerCart;

import java.util.List;

public interface CartService {
    List<BuyerCart> addItemToBuyerCartList(List<BuyerCart> cartList, Long itemId, Integer num);

    List<BuyerCart> mergeBuyerCartListFromCookieAndRedis(String cartListJsonStrFromCookie, List<BuyerCart> cartListFromRedis);
//    保存购物车集合到redis
    void saveBuyerCartListToRedis(List<BuyerCart> cartList, String userName);
    //    取出购物车集合from redis
    List<BuyerCart> getBuyerCartListFromRedis(String userName);

}
