package cn.itcast.core.service;


import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.user.User;

import java.util.List;

public interface UserService {



    void add(User user);



   // Integer queryTotalCount();


    void sendCode(String phone);

    boolean checkSmsCode(String phone, String smsCode);
}
