package cn.itcast.core.service;

import cn.itcast.core.pojo.address.Address;

import java.util.List;

public interface AddressService {
    List<Address> findAddressListByLoginUser(String userName);

    void addAddress(Address address);

    void delAddress(Address address);

    void updateAddress(Address address);
}
