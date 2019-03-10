package cn.itcast.core.service;

import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.dao.seckill.SeckillOrderDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import cn.itcast.core.pojo.seckill.SeckillOrder;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class KillServiceCYHImplCYH implements KillGoodsServiceCYH {

    @Autowired
    private SeckillGoodsDao goodsDao;

    @Autowired
    private SeckillOrderDao orderDao;
    @Override
    public PageResult findPage(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        SeckillGoodsQuery query = new SeckillGoodsQuery();
        SeckillGoodsQuery.Criteria criteria = query.createCriteria();
        criteria.andStatusEqualTo("0");
        Page<SeckillGoods> goodsList =(Page<SeckillGoods>) goodsDao.selectByExample(query);
        return new PageResult(goodsList.getTotal(), goodsList.getResult());
    }

    @Override
    public PageResult findOrder(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        Page<SeckillOrder> seckillOrders = (Page<SeckillOrder>) orderDao.selectByExample(null);
        return new PageResult(seckillOrders.getTotal(),seckillOrders.getResult());
    }
}
