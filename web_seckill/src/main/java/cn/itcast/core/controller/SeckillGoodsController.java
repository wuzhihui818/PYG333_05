package cn.itcast.core.controller;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("seckillGoods")
@RestController
public class SeckillGoodsController {
    @Reference
    private SeckillService seckillService;
    @RequestMapping("findList")
    public List<SeckillGoods> findList(){
        List<SeckillGoods> seckillGoodsList= seckillService.findList();
        return seckillGoodsList;
    }
}
