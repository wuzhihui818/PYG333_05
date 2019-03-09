package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SeckillService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("seckillCart")
public class SeckillCartController {
    @Reference
    private SeckillService seckillService;

    /**
     * 查询已经秒杀到的订单
     * wuzhihui
     */
    @RequestMapping("findSeckillOrder")
    public SeckillOrder findSeckillOrder(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        SeckillOrder seckillOrder= seckillService.findSeckillOrder(name);
        return seckillOrder;
    }
}
