package cn.itcast.core.controller;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.service.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("address")
public class SeckillAddressController {
    @Reference
    private AddressService addressService;
    /**
     * 秒杀提交订单时的收货地址列表
     */
    @RequestMapping("findListByLoginUser")
    public List<Address> findListByLoginUser(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Address> addressList = addressService.findAddressListByLoginUser(name);
//        System.out.println(addressList);
        return addressList;
    }

}
