package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;

public interface KillGoodsService {
    PageResult findPage(Integer page, Integer rows);

}
