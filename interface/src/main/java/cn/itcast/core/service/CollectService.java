package cn.itcast.core.service;

import cn.itcast.core.pojo.item.Item;

import java.util.List;

public interface CollectService {
    void addGoodsToCollectList(Long itemId,String name);

    List<Item> findItemList(String name);
}
