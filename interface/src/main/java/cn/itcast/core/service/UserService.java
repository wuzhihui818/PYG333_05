package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.user.User;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {

    public void sendCode(String phone);

    public boolean checkSmsCode(String phone, String smsCode);

    public  void  add(User user);



    public PageResult search(String userName,Integer page, Integer rows ,  Order order);
    public List<Order> findAll(String userName);

    //根据用户名查找用户信息
    public List<User> findOneByuserName(String userName);

        //根据用户名和页面传递来的user对象,update
    public  void save(String userName, User user);
}
