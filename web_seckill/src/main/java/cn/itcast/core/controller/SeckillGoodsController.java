package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("seckillGoods")
@RestController
public class SeckillGoodsController {
    @Reference
    private SeckillService seckillService;


    /**
     * @author wuzhihui
     * @return
     * 显示参加秒杀的商品列表
     *
     */
    @RequestMapping("findList")
    public List<SeckillGoods> findList(){
        List<SeckillGoods> seckillGoodsList= seckillService.findList();
            return seckillGoodsList;
    }

    /**
     * wuzhihui
     * 显示秒杀商品详请
     * @param id
     * @return
     */
    @RequestMapping("findOneFromRedis")
    public SeckillGoods findOneFromRedis(Long id){
       SeckillGoods seckillGoods=seckillService.findOneFromRedis(id);
       return seckillGoods;

    }

}
