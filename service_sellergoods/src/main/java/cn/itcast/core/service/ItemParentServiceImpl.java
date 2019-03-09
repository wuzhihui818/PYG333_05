package cn.itcast.core.service;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

@Service
public class ItemParentServiceImpl implements ItemParentService {

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        List<ItemCat> redisList = (List<ItemCat>) redisTemplate.boundHashOps("itemCat").get("itemCatParent");
        if (redisList == null) {
            List<ItemCat> itemCatList = itemCatDao.findItemCatListByParentId(parentId);
            for (ItemCat itemCat : itemCatList) {
                List<ItemCat> itemCatList2 = itemCatDao.findItemCatListByParentId(itemCat.getId());
                for (ItemCat cat : itemCatList2) {
                    List<ItemCat> itemCatList3 = itemCatDao.findItemCatListByParentId(cat.getId());
                    cat.setItemCatList(itemCatList3);
                }
                itemCat.setItemCatList(itemCatList2);
            }
            redisTemplate.boundHashOps("itemCat").put("itemCatParent", itemCatList);
            return itemCatList;
        }
        return redisList;
    }

}







