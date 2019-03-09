package cn.itcast.core.service;

import cn.itcast.core.common.Constants;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        /**
         * 查询
         */
        ItemCatQuery query = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = query.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> itemCats = itemCatDao.selectByExample(query);

        /**
         * 缓存所有分类数据到redis中, 供portal系统搜索使用
         */
        List<ItemCat> catList = itemCatDao.selectByExample(null);
        if (catList != null) {
            for (ItemCat itemCat : catList) {
                redisTemplate.boundHashOps(Constants.REDIS_CATEGORY).put(itemCat.getName(), itemCat.getTypeId());
            }
        }


        return itemCats;
    }

    @Override
    public ItemCat findOne(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }

    @Override
    public List<ItemCat> findAll() {
        return itemCatDao.selectByExample(null);
    }

     //修改分类状态数据
    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids != null){
            for (Long id : ids) {
        ItemCat itemCat = new ItemCat();
        itemCat.setStatus(status);
             itemCat.setId(id);
          itemCatDao.updateByPrimaryKeySelective(itemCat);

         }
     }
    }

    @Override
    public void addItemCat(ItemCat itemCat) {
        itemCatDao.insertSelective(itemCat);
    }

    @Override
    public PageResult findPage(ItemCat itemCat, Integer page, Integer rows) {
        //创建查询对象
        ItemCatQuery query = new ItemCatQuery();
        //组装条件
        query.createCriteria().andStatusEqualTo("0");
        PageHelper.startPage(page, rows);
        //查询
        Page<ItemCat> itemCatList = (Page<ItemCat>)itemCatDao.selectByExample(query);
        return new PageResult(itemCatList.getTotal(), itemCatList.getResult());
    }

    @Override
    public void add(ItemCat itemCat) {
        itemCat.setStatus("0");
        itemCatDao.insertSelective(itemCat);
    }
}
