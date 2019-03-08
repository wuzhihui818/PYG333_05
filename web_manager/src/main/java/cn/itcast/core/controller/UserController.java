package cn.itcast.core.controller;

import cn.itcast.core.common.PhoneFormatCheckUtils;
import cn.itcast.core.pojo.entity.PageResult;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;
    /**
     * 查询品牌所有数据
     * @return
     */
    @RequestMapping("findAll")
    public List<User>findAll(){

        List<User> userList = userService.findAll();
        return userList;

    }
    @RequestMapping("add")
    public Result add (@RequestBody User user){

        try {
            userService.add(user);
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
    @RequestMapping("search")
    public PageResult search(Integer page,Integer rows,@RequestBody User user){

        PageResult pageResult = userService.findPage(page, rows, user);
        return pageResult;

    }
    @RequestMapping("delete")
    public Result delete(Long[]ids){

        try {
            userService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }

    }
    @RequestMapping("sta1")
    public Integer count(){

        return userService.queryTotalCount();

    }
/*    @RequestMapping("sta1")
    public List<User>sta1(){

        List<User> userList = userService.sta1();
        return userList;

    }
    @RequestMapping("sta2")
    public List<User>sta2(){

        List<User> userList = userService.sta2();
        return userList;

    }*/
/*    @RequestMapping("sta1")
    public Integer sta1(){

        return userService.sta1Count();

    }*/
}
