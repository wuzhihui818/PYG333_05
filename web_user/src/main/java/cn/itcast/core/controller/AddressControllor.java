package cn.itcast.core.controller;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.AddressService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
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


    /**
     * 编辑界面的数据回显
     * @param id
     * @return
     */
    @RequestMapping("findOne")
    public Address findOne(Long id){
        return addressService.findOne(id);
    }



    /**
     * 当前用户增加一个地址
     */
    @RequestMapping("addAddress")
    public Result addAddress(@RequestBody Address address){

        System.out.println(address);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        address.setUserId(userName);
        try {
            addressService.addAddress(address);
            return new Result(true,"增加地址成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"增加地址失败");
        }

    }

    /**
     * 当前用户删除一个地址
     */
    @RequestMapping("delAddress")
    public Result delAddress(Long id){
//        System.out.println(id);
        try {
            addressService.delAddress(id);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }

    }


    /**
     * 当前用户修改地址
     */
    @RequestMapping("updateAddress")
    public Result updateAddress(@RequestBody Address address){
//        System.out.println(address);
        try {
            addressService.updateAddress(address);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,"修改失败");
        }

    }


    /**
     * 当前用户修改默认地址
     */
    @RequestMapping("morenAddress")
    public Result morenAddress(Long id){
//        System.out.println(id);
        try {
            addressService.morenAddress(id);
            return new Result(true,"修改默认地址成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改默认地址失败");
        }

    }









}
