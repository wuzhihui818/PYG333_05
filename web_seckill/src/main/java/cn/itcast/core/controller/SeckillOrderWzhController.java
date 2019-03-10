package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import cn.itcast.core.service.SeckillService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("seckillOrder")
public class SeckillOrderWzhController {
    @Reference
    private SeckillService seckillService;

    /**
     * 秒杀订单下单
     * wuzhihui
     *
     * @param seckillId
     * @return
     */
    @RequestMapping("submitOrder")
    public Result submitOrder(Long seckillId) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(name)) {
            return new Result(false, "请先登录后再操作");
        }
        try {
            seckillService.submitOrder(seckillId, name);
            return new Result(true, "下单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "宝贝已经被抢光了");
        }

    }

    /**
     * wuzhihui
     * 添加订单到支付页面
     * @return
     */
    @RequestMapping("addOrder")
    public Result addOrder(@RequestBody SeckillOrder seckillOrder) {
        try {
            seckillService.addOrder(seckillOrder);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");

        }
    }


}
