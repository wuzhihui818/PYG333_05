package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface UserService {


    public List<User>findAll();

    //PageResult findPage(Integer page, Integer rows);

//    void add(User user);

    User findOne(Long id);

    void update(User user);

    void delete(Long[] ids);

    PageResult findPage(Integer page, Integer rows, User user);

    public  void  add(User user);

    PageResult search(Integer page, Integer rows, User user);

    void updateStatus(Long[] ids, String status);

    List<User> findOne(String username);

    List<User> findAlluser();

    Integer queryTotalCount();

    Integer sta1Count();

//    List<User> sta1();

//    List<User> sta2();
}
