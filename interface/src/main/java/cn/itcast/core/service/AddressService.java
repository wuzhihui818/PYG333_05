package cn.itcast.core.service;

import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.Areas;
import cn.itcast.core.pojo.address.Cities;
import cn.itcast.core.pojo.address.Provinces;

import java.util.List;

public interface AddressService {
    List<Address> findAddressListByLoginUser(String userName);

    void addAddress(Address address);

    void delAddress(Long id);

    void updateAddress(Address address);

    Address findOne(Long id);

    void morenAddress(Long id);

    List<Provinces> findProvince(String parentId);

    List<Cities> findCity(String parentId);

    List<Areas> findArea(String parentId);
}
