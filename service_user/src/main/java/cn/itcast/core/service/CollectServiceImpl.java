package cn.itcast.core.service;

import cn.itcast.core.dao.collect.CollectDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.collect.Collect;
import cn.itcast.core.pojo.collect.CollectQuery;
import cn.itcast.core.pojo.item.Item;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {
    @Autowired
    private CollectDao collectDao;
    @Autowired
    private ItemDao itemDao;

    /**
     * 添加商品到收藏列表
     * @param itemId
     * @param
     */
    @Override
    public void addGoodsToCollectList(Long itemId,String name) {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        if ("anonymousUser".equals(name)){
//            throw new RuntimeException("尚未登录");
//        }else {
            Collect collect = new Collect();
            collect.setItemid(itemId);
            collect.setUsername(name);
            collectDao.insertSelective(collect);
//        }



    }

    /**
     * 根据用户名获取收藏列表,再根据收藏列表中的itemId查找商品的库存详请,返回商品的库存集合
     *
     * @return
     */
    @Override
    public List<Item> findItemList(String name) {
        CollectQuery collectQuery = new CollectQuery();
        CollectQuery.Criteria criteria = collectQuery.createCriteria();
        criteria.andUsernameEqualTo(name);
        List<Collect> collects = collectDao.selectByExample(collectQuery);
//        遍历收藏集合.根据集合中的itemId查询商品详请封装到list集合中
        List<Item> items = new ArrayList<>();
        if (collects != null && collects.size() > 0) {
            for (Collect collect : collects) {
                Item item = itemDao.selectByPrimaryKey(collect.getItemid());
                items.add(item);
            }
        }
        return items;
    }
}
