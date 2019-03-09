package cn.itcast.core.service;

import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemParentService {
    public List<ItemCat> findByParentId(Long parentId);
}
