package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.service.SeckillServiceDrx;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seckill")
public class SeckillControllerDrx {

     @Reference
     private SeckillServiceDrx seckillServiceDrx;
       //秒杀订单商品 分页查询数据
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody SeckillGoods seckillGoods) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
          if ( name != null){
              seckillGoods.setSellerId(name);
              return seckillServiceDrx.search(page,rows,seckillGoods);

          }
          return  null;
    }
}
