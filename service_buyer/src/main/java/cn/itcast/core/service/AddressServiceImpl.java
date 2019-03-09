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

    /**
     * 查询用户所有地址
     * @param userName
     * @return
     */
    @Override
    public List<Address> findAddressListByLoginUser(String userName) {
        AddressQuery addressQuery = new AddressQuery();
        AddressQuery.Criteria criteria = addressQuery.createCriteria();
        criteria.andUserIdEqualTo(userName);
        List<Address> addressList = addressDao.selectByExample(addressQuery);
        return addressList;
    }

    /**
     * 用户添加地址
     * @param address
     */
    @Override
    public void addAddress(Address address) {
        addressDao.insertSelective(address);
    }

    /**
     * 用户删除地址
     * @param id
     */
    @Override
    public void delAddress(Long id) {
        addressDao.deleteByPrimaryKey(id);


    }

    /**
     * 当前用户修改地址
     */
    @Override
    public void updateAddress(Address address) {
        Long id = address.getId();
       addressDao.updateByPrimaryKey(address);


    }

    /**
     * 编辑界面的数据回显
     * @param id
     * @return
     */
    @Override
    public Address findOne(Long id) {
        return addressDao.selectByPrimaryKey(id);
    }


    /**
     * 用户修改默认地址
     * @param id
     */
    @Override
    public void morenAddress(Long id) {
        /**
         * 将原来的默认地址设为非默认
         */
        Address address=new Address();
        address.setIsDefault("0");
        AddressQuery query = new AddressQuery();
        AddressQuery.Criteria criteria = query.createCriteria();
        criteria.andIsDefaultEqualTo("1");
        addressDao.updateByExampleSelective(address,query);
        /**
         * 将页面传过来的地址id设为默认地址
         */
        Address address1 = new Address();
        address1.setId(id);
        address1.setIsDefault("1");
        addressDao.updateByPrimaryKeySelective(address1);
    }

}
