package cn.itcast.core.service;

import cn.itcast.core.pojo.seckill.SeckillGoods;

import java.util.List;

public interface SeckillService {
    void updateStatus(Long[] ids, String status);

    List<SeckillGoods> findList();


}
