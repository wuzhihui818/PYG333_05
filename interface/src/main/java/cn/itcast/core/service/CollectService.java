package cn.itcast.core.service;

import cn.itcast.core.pojo.collect.Collect;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.item.Item;

import java.util.List;

public interface CollectService {
    void addGoodsToCollectList(Long itemId,String name);

    List<Item> findItemList(String name);

    public void addGoodsToCollect (Long itemId,String userName);
}
