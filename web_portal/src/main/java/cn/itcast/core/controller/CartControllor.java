package cn.itcast.core.controller;

import cn.itcast.core.common.CookieUtil;
import cn.itcast.core.pojo.entity.BuyerCart;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("cart")
public class CartControllor {
    @Reference
    private CartService cartService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;


    /**
     * 添加物品到购物车
     *
     * @param itemId 库存id
     * @param num    添加数量
     * @return
     */
    @CrossOrigin(origins = "http://localhost:8086", allowCredentials = "true")
    @RequestMapping("addGoodsToCartList")
    public Result addGoodsToCartList(Long itemId, Integer num) {

        try {
//        1.获取用户名,如果用户名为"anonymousUser",则表示其未登陆
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
//        2.通过findCartList方法获得购物车列表
            List<BuyerCart> cartList = findCartList();
//        3.将商品添加到购物车列表中
            cartList = cartService.addItemToBuyerCartList(cartList, itemId, num);
            String cartListJsonStr = JSON.toJSONString(cartList);
//        4.已登录则把商品存到redis中,未登陆存到cookie中
            if ("anonymousUser".equals(userName)) {
                //        存入cookie中
                CookieUtil.setCookie(request, response, Fields.CARTLIST_COOKIE, cartListJsonStr, 60 * 60 * 24, "UTF-8");
            } else {
                //        存入redis中,键名为用户名称userName
                cartService.saveBuyerCartListToRedis(cartList, userName);
            }
            return new Result(true, "添加购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加购物车失败");
        }
    }

    /**
     * 回显购物车列表
     *
     * @return
     */
    @RequestMapping("findCartList")
    public List<BuyerCart> findCartList() {
//      1.获取当前用户名,判断是否登录,"anonymousUser"表示未登陆
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(userName)) {
//      2.如果未登陆,从cookie中获取购物车列表
            String cartListJsonStr = CookieUtil.getCookieValue(request, Fields.CARTLIST_COOKIE, "UTF-8");
            if (cartListJsonStr == null || "".equals(cartListJsonStr)) {
                cartListJsonStr = "[]";
            }
            List<BuyerCart> cartList = JSON.parseArray(cartListJsonStr, BuyerCart.class);
            return cartList;
        } else {
//        3.如果已经登录,则从cookie和redis中获取购物车列表,然后合并到一起
            String cartListJsonStr = CookieUtil.getCookieValue(request, Fields.CARTLIST_COOKIE, "UTF-8");
            List<BuyerCart> cartList = cartService.getBuyerCartListFromRedis(userName);
//          将cookie和redis中的购物车列表合并到一起
            cartList = cartService.mergeBuyerCartListFromCookieAndRedis(cartListJsonStr, cartList);
//            将新的合并后的购物车集合存入redis中,并将cookie删除
            cartService.saveBuyerCartListToRedis(cartList, userName);
            CookieUtil.deleteCookie(request, response, Fields.CARTLIST_COOKIE);
//        4.返回 购物车列表
            return cartList;
        }
    }
}
