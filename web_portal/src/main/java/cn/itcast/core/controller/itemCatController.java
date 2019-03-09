package cn.itcast.core.controller;

import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.service.ItemCatService;
import cn.itcast.core.service.ItemParentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class itemCatController {

        @Reference
        private ItemParentService itemParentService;

        /**
         * 查询商品分类信息
         *
         * @return
         */
        @RequestMapping("/findByParentId")
        public List<ItemCat> findByParentId(Long parentId) {
            List<ItemCat> itemCatList = itemParentService.findByParentId(parentId);
            return itemCatList;
        }

}
