package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;

import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import org.springframework.beans.factory.annotation.Autowired;




import java.util.Date;

import java.util.List;

/**
 * 张锋
 */
@Service
public class UserServiceImpl_ZhangFeng implements UserService_ZhangFeng {
    @Autowired
    private UserDao userDao;
    /**
     * 张锋
     */
    @Override
    public List<User> findAll() {
        List<User> userList = userDao.selectByExample(null);

        return userList;
    }
    /**
     * 张锋
     */
    @Override
    public void add(User user) {
        user.setStatus("0");
        user.setPassword("0");
        user.setCreated(new Date());
        user.setEmail("0");
        user.setBirthday(new Date());
        user.setHeadPic("0");
        user.setAccountBalance(0l);
        user.setUpdated(new Date());
        user.setNickName("0");
        user.setIsEmailCheck("0");
        user.setSex("1");
        user.setSourceType("0");
        user.setName("0");
        user.setQq("0");
        user.setUserLevel(0);
        user.setPoints(0);
        user.setIsMobileCheck("0");
        user.setLastLoginTime(new Date());
        user.setPhone("0");
        user.setExperienceValue(0);
        userDao.insertSelective(user);
    }
    /**
     * 张锋
     */
    @Override
    public User findOne(Long id) {
        User user = userDao.selectByPrimaryKey(id);
        return user;
    }
    /**
     * 张锋
     */
    @Override
    public void update(User user) {
        userDao.updateByPrimaryKeySelective(user);
    }
    /**
     * 张锋
     */
    @Override
    public void delete(Long[] ids) {
        if(ids!=null){

            for (Long id : ids) {
                userDao.deleteByPrimaryKey(id);
            }
        }
    }
    /**
     * 张锋
     */
    @Override
    public PageResult findPage(Integer page, Integer rows, User user) {
        UserQuery userQuery=new UserQuery();
        PageHelper.startPage(page,rows);
        UserQuery.Criteria criteria = userQuery.createCriteria();
        if(user!=null){
            if (user.getStatus() != null && !"".equals(user.getStatus())) {

                criteria.andStatusEqualTo(user.getStatus());

            }
        }
        //查询
        Page<User> userList =(Page<User>) userDao.selectByExample(userQuery);
        return new PageResult(userList.getTotal(),userList.getResult());
    }



}
