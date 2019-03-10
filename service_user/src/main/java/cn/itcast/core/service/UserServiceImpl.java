package cn.itcast.core.service;

import cn.itcast.core.common.Constants;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.entity.PageResult;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.pojo.user.UserQuery;

import cn.itcast.core.pojo.user.UserQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQQueue;


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
    private RedisTemplate redisTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ActiveMQQueue smsDestination;

    @Value("${template_code}")
    private String template_code;
    @Value("${sign_name}")
    private String sign_name;

    @Autowired
    private UserDao userDao;
    @Override
    public List<User> findAll() {
        List<User> userList = userDao.selectByExample(null);

        return userList;
    }

    @Override
    public void sendCode(final String phone) {
        //1. 生成随机6位以内的数字作为验证码
        final String code = String.valueOf((long) (Math.random() * 1000000));
        //2. 将手机号作为key, 验证码作为value存入redis中
        redisTemplate.boundValueOps(phone).set(code, 10, TimeUnit.MINUTES);
        //3. 将手机号, 验证码, 签名, 模板编号等信息组成消息对象发送给消息服务器
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = new ActiveMQMapMessage();
                //手机号
                mapMessage.setString("mobile", phone);
                //模板编号
                mapMessage.setString("template_code", template_code);
                //签名
                mapMessage.setString("sign_name", sign_name);

                //验证码
                Map<String, String> codeMap = new HashMap<>();
                codeMap.put("code", code);
                mapMessage.setString("param", JSON.toJSONString(codeMap));
                return mapMessage;
            }
        });
    }

    @Override
    public boolean checkSmsCode(String phone, String smsCode) {
        //1. 判断手机号和验证码不为空
        if (phone == null || "".equals(phone) || smsCode == null || "".equals(smsCode)) {
            return false;
        }
        //2. 根据手机号到redis中获取我们保存的验证码
        String redisSmsCode = (String) redisTemplate.boundValueOps(phone).get();
        //3. 判断使用手机号是否能够取出验证码
        if (redisSmsCode == null || "".equals(redisSmsCode)) {
            return false;
        }
        //4. 判断我们的验证码和页面传入的验证码是否一致
        if (!smsCode.equals(redisSmsCode)) {
            return false;
        }
        return true;
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
    public PageResult search(Integer page, Integer rows, User user) {
        //创建查询对象
        UserQuery query = new UserQuery();
        //组装条件
        if (user != null) {
            //创建sql语句中的where条件对象
            UserQuery.Criteria criteria = query.createCriteria();
            if (user.getId() != null && !"".equals(user.getId())) {
                criteria.andIdEqualTo(user.getId());
            }
            if (user.getName() != null && !"".equals(user.getName())) {
                criteria.andNameLike("%" + user.getName() + "%");
            }
        }
        PageHelper.startPage(page, rows);
        //查询
        Page<User> userList = (Page<User>) userDao.selectByExample(query);
        return new PageResult(userList.getTotal(), userList.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        if (ids != null) {
            for (Long id : ids) {
                User user = new User();
                user.setId(id);
                user.setStatus(status);
                userDao.updateByPrimaryKeySelective(user);
            }
        }
    }

    @Override
    public List<User> findOne(String username) {
        UserQuery query = new UserQuery();
        UserQuery.Criteria criteria = query.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> users = userDao.selectByExample(query);
        return users;

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

    @Override
    public List<User> findAlluser() {
        return null;
    }

    @Override
    public Integer queryTotalCount() {
        return null;
    }

    @Override
    public Integer sta1Count() {
        return null;
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
        return null;
    }
}