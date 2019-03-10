package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface UserService {

    public void sendCode(String phone);

    public boolean checkSmsCode(String phone, String smsCode);

    public  void  add(User user);

    public List<User>findAll();
//    User findOne(Long id);
//   void update(User user);
///*
//    void delete(Long[] ids);

//    PageResult findPage(Integer page, Integer rows, User user);

//    public  void  add(User user);

    PageResult search(Integer page, Integer rows, User user);

    void updateStatus(Long[] ids, String status);

    List<User> findOne(String username);

//    List<User> findAlluser();
//
//    Integer queryTotalCount();
//
//    Integer sta1Count();
}
