package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface UserService {

    public void sendCode(String phone);

    public boolean checkSmsCode(String phone, String smsCode);

    public  void  add(User user);

    PageResult search(Integer page, Integer rows, User user);

    void updateStatus(Long[] ids, String status);

    List<User> findOne(String username);
}
