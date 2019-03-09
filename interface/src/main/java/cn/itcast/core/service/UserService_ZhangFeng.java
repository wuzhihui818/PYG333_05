package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;

import java.util.List;
/*
张锋
 */
public interface UserService_ZhangFeng {

    /*
    张锋
     */
    public List<User>findAll();

        /*
    张锋
     */
    void add(User user);
    /*
    张锋
     */
    User findOne(Long id);
    /*
    张锋
     */
    void update(User user);
    /*
    张锋
     */
    void delete(Long[] ids);
    /*
    张锋
     */
    PageResult findPage(Integer page, Integer rows, User user);

}
