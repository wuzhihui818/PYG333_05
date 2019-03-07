package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.PayService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("pay")
public class PayController {
    @Reference
    private PayService payService;
    /**
     * 修改创建二维码的方法
     */

    /**
     * 创建支付二维码
     *
     * @return
     */
    @RequestMapping("createNative")
    public Map createNative() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = payService.creatNative(userName);
        System.out.println(map);
        return map;
    }

    /**
     * 查询支付状态,如果已经支付成功,就把数据库中的status支付状态全部改为已支付,并删除redis中的支付日志信息
     * @return
     */
    @RequestMapping("queryPayStatus")
    public Result queryPayStatus() {
        try {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            String status = payService.queryPayStatus(userName);
            if ("SUCCESS".equals(status)){
                return new Result(true,"支付成功");
            }else {
                return new Result(false,"二维码超时");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"支付失败");

        }
    }
}
