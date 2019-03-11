package cn.itcast.core.pojo.entity;

import cn.itcast.core.pojo.seckill.SeckillGoods;

import java.io.Serializable;

public class SeckillEntityDrx implements Serializable {
    //分装 秒杀数据
     private SeckillGoods seckillGoods;

    public SeckillGoods getSeckillGoods() {
        return seckillGoods;
    }

    public void setSeckillGoods(SeckillGoods seckillGoods) {
        this.seckillGoods = seckillGoods;
    }
}
