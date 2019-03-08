package cn.itcast.core.util;

import cn.itcast.core.dao.ad.ContentCategoryDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.ad.ContentCategory;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceForData {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ContentCategoryDao contentCategoryDao;


    @Autowired
    private GoodsDao goodsDao;

    public List<User> findAlluser(){
        List<User> users = userDao.selectByExample(null);
        return users;
    }
    public List<ContentCategory> findCate(){
        List<ContentCategory> contentCategories = contentCategoryDao.selectByExample(null);
        return contentCategories;
    }

    public List<Goods> findGoods(){
        List<Goods> goods = goodsDao.selectByExample(null);
        return goods;
    }

    public  static List<User> getUserList(){
        //1. 创建spring运行环境对象, 加载spring配置文件
        ApplicationContext application = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        //2. 获取当前类对象
        UserServiceForData userServiceForData = (UserServiceForData) application.getBean("userServiceForData");
        List<User> list = userServiceForData.findAlluser();
        System.out.println(list);
        return list;
    }
}
