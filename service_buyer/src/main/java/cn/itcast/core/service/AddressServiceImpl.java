package cn.itcast.core.service;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.AddressQuery;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressDao addressDao;
    @Override
    public List<Address> findAddressListByLoginUser(String userName) {
        AddressQuery addressQuery = new AddressQuery();
        AddressQuery.Criteria criteria = addressQuery.createCriteria();
        criteria.andUserIdEqualTo(userName);
        List<Address> addressList = addressDao.selectByExample(addressQuery);
        return addressList;
    }

    @Override
    public void addAddress(Address address) {
        addressDao.insertSelective(address);
    }

    @Override
    public void delAddress(Address address) {
        Long id = address.getId();
        if (id!=null){
            addressDao.deleteByPrimaryKey(id);
        }

    }

    @Override
    public void updateAddress(Address address) {
        Long id = address.getId();
        addressDao.deleteByPrimaryKey(id);

        addressDao.insertSelective(address);


    }
}
