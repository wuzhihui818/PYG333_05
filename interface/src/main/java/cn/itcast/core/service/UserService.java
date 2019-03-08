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

    public void addUser(String userName,User user);

    public PageResult search(String userName,Integer page, Integer rows ,  Order order);
    public List<Order> findAll(String userName);






}
