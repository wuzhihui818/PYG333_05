package cn.itcast.core.controller;

import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService_ZhangFeng;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 张锋
 */
@RestController
@RequestMapping("/user")
public class UserController_ZhangFeng {
    @Reference
    private UserService_ZhangFeng userService_zhangFeng;
    /**
     * 查询品牌所有数据
     * @return
     */
    /**
     * 张锋
     */
    @RequestMapping("findAll")
    public List<User>findAll(){

        List<User> userList = userService_zhangFeng.findAll();
        return userList;

    }
    /**
     * 张锋
     */
    @RequestMapping("add")
    public Result add (@RequestBody User user){

        try {
            userService_zhangFeng.add(user);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }

    }

    /**
     * 高级查询(分页,高级查询)
     * @param page  当前页
     * @param rows  每页展示多少条数据
     * @param user 需要查询的条件的品牌对象
     * @return
     */
    /**
     * 张锋
     */
    @RequestMapping("search")
    public PageResult search(Integer page,Integer rows,@RequestBody User user){

        PageResult pageResult = userService_zhangFeng.findPage(page, rows, user);
        return pageResult;

    }
    /**
     * 张锋
     */
    @RequestMapping("delete")
    public Result delete(Long[]ids){

        try {
            userService_zhangFeng.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }

    }
}
