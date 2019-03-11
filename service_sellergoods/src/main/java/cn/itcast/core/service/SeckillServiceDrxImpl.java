package cn.itcast.core.service;

import cn.itcast.core.dao.seckill.SeckillGoodsDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.seckill.SeckillGoods;
import cn.itcast.core.pojo.seckill.SeckillGoodsQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeckillServiceDrxImpl implements  SeckillServiceDrx {
    @Autowired
    private SeckillGoodsDao seckillGoodsDao;
 //秒杀分页查询数据
    @Override
    public PageResult search(Integer page, Integer rows, SeckillGoods seckillGoods) {
        PageHelper.startPage(page, rows);
        Page<SeckillGoods> seckillGoodsList = (Page<SeckillGoods>) seckillGoodsDao.selectByExample(null);
        return new PageResult(seckillGoodsList.getTotal(),seckillGoodsList.getResult());
    }
}
