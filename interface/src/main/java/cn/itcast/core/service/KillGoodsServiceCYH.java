package cn.itcast.core.service;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;

public interface KillGoodsServiceCYH {
    PageResult findPage(Integer page, Integer rows);

    PageResult findOrder(Integer page, Integer rows);
}
