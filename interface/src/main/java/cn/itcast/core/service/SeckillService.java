package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillOrder;

import java.util.List;

public interface SeckillService {
    void updateStatus(Long[] ids, String status);

    List<SeckillGoods> findList();


    SeckillGoods findOneFromRedis(Long id);

    void submitOrder(Long seckillId,String name);

    SeckillOrder findSeckillOrder(String name);

    void addOrder(SeckillOrder seckillOrder);
}
