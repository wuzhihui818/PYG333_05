package cn.itcast.core.service;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;

import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public List<User> findAll() {
        List<User> userList = userDao.selectByExample(null);

        return userList;
    }

    @Override
    public void add(User user) {
        user.setStatus("0");
        user.setPassword("0");
        user.setCreated(new Date());
        user.setEmail("0");
        user.setBirthday(new Date());
        user.setHeadPic("0");
        user.setAccountBalance(0L);
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
       // userDao.insert(user);
    }

    @Override
    public User findOne(Long id) {
        User user = userDao.selectByPrimaryKey(id);
        return user;
    }

    @Override
    public void update(User user) {
        userDao.updateByPrimaryKeySelective(user);
    }

    @Override
    public void delete(Long[] ids) {
        if(ids!=null){

            for (Long id : ids) {
                userDao.deleteByPrimaryKey(id);
            }
        }
    }

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


    @Override
    public List<User> findAlluser() {
        List<User> users = userDao.selectByExample(null);
        return users;
    }

    @Override
    public Integer queryTotalCount() {
       // UserQuery userQuery = new UserQuery();
        //return userDao.countByExample(userQuery);
        return userDao.queryTotalCount();
    }

    @Override
    public Integer sta1Count() {
        UserQuery userQuery = new UserQuery();
        UserQuery.Criteria criteria = userQuery.createCriteria();
        criteria.andStatusEqualTo("0");
        return userDao.countByExample(userQuery);
    }

//    @Override
//    public List<User> sta1() {
//
//        UserQuery userQuery = new UserQuery();
//        UserQuery.Criteria criteria = userQuery.createCriteria();
//        criteria.andStatusEqualTo("0");
//        List<User> userList = userDao.selectByExample(userQuery);
//
//
//        return userList;
//       // return null;
//    }
//
//    @Override
//    public List<User> sta2() {
//        UserQuery userQuery = new UserQuery();
//        UserQuery.Criteria criteria = userQuery.createCriteria();
//        criteria.andStatusEqualTo("1");
//        List<User> userList = userDao.selectByExample(userQuery);
//
//
//        return userList;
//
}
