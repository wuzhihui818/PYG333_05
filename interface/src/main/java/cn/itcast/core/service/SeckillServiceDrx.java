package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import org.springframework.web.bind.annotation.RequestBody;

public interface SeckillServiceDrx {
    public PageResult search(Integer page, Integer rows,  SeckillGoods seckillGoods);
}
