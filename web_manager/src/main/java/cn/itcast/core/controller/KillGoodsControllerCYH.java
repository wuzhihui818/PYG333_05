package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.service.KillGoodsServiceCYH;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("goodsKill")
public class KillGoodsControllerCYH {

    @Reference
    private KillGoodsServiceCYH killGoodsServiceCYH;



    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods) {
        PageResult pageResult = killGoodsServiceCYH.findPage(page, rows);
        return pageResult;
    }


}
