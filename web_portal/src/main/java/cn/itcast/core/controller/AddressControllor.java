package cn.itcast.core.controller;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.service.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("address")
@RestController
public class AddressControllor {
    @Reference
    private AddressService addressService;
    /**
     * 获取当前用户的收货地址集合
     * @return
     */
    @RequestMapping("findListByLoginUser")
    public List<Address> findListByLoginUser(){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
       List<Address> addressList = addressService.findAddressListByLoginUser(userName);
           return addressList;
    }


}
