package cn.itcast.core.task;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.entity.Fields;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class SeckillTaskWuZhihui {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsDao seckillGoodsDao;

    /**
     * 添加商品到redis
     * 每分钟刷新一次
     *
     * @author wuzhihui
     */
    @Scheduled(cron = "0 * * * * ?")
    public void refreshSeckillGoods() {

        System.out.println("执行了任务调度" + new Date());

        //查询所有的秒杀商品键集合

        List ids = new ArrayList(redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).keys());

        //查询正在秒杀的商品列表

        SeckillGoodsQuery seckillGoodsQuery = new SeckillGoodsQuery();

        SeckillGoodsQuery.Criteria criteria = seckillGoodsQuery.createCriteria();

        criteria.andStatusEqualTo("1");//审核通过

        criteria.andStockCountGreaterThan(0);//剩余库存大于0


        criteria.andStartTimeLessThanOrEqualTo(new Date());//开始时间小于等于当前时间

        criteria.andEndTimeGreaterThan(new Date());//结束时间大于当前时间

        criteria.andIdNotIn(ids);//排除缓存中已经有的商品

        List<SeckillGoods> seckillGoodsList = seckillGoodsDao.selectByExample(null);
//        装入缓存
        if (seckillGoodsList != null && seckillGoodsList.size() > 0) {

            for (SeckillGoods seckill : seckillGoodsList) {

                redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).put(seckill.getId(), seckill);

                System.out.println("添加了商品到redis");
            }
        }



    }

    /**
     * 每秒刷新一次,发现过期同步到数据库，并在缓存中移除该秒杀商品
     *
     * @author wuzhihui
     */
    @Scheduled(cron = "* * * * * ?")
    public void removeSeckillGoods() {

        System.out.println("移除秒杀商品任务在执行" + new Date());

        //扫描缓存中秒杀商品列表，发现过期的移除

        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).values();

        for (SeckillGoods seckill : seckillGoodsList) {

            if (seckill.getEndTime().getTime() < new Date().getTime()) {//如果结束日期小于当前日期，则表示过期

                seckillGoodsDao.updateByPrimaryKey(seckill);//向数据库保存记录

                redisTemplate.boundHashOps(Fields.SECKILLGOODS_REDIS).delete(seckill.getId());//移除缓存数据

                System.out.println("移除秒杀商品" + seckill.getId());

            }

        }

    }

}