package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.SeckillService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("seckill")
@RestController
public class SeckillController {
    @Reference
    private SeckillService seckillService;
    /**
     * s审核秒杀申请,并修改状态
     * @return
     * @author wuzhihui
     */
    @RequestMapping("updateStatus")
    public Result updateStatus(Long[] ids,String status){
        try {
            seckillService.updateStatus(ids,status);
            return new Result(true,"审核成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"审核失败");
        }
    }
}
