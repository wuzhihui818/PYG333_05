package cn.itcast.core.controller;

import cn.itcast.core.common.ExcelUtil;
import cn.itcast.core.pojo.ad.ContentCategory;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.service.ContentCategoryService;
import cn.itcast.core.service.GoodsService;
import cn.itcast.core.service.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("data")
public class DataController {

    @Reference
    private UserService userService;

    @Reference
    private ContentCategoryService contentCategoryService;

    @Reference
    private GoodsService goodsService;


    @RequestMapping("getData")
    public void exportExcelData(HttpServletRequest request, HttpServletResponse response){
        //获取后台数据
        List<User> userList = userService.findAlluser();
        // 定义表的标题
        String title = "User数据";
        //定义表的列名
        String[] rowsName = new String[] { "id", "username", "password", "phone", "create", "updated"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (User user : userList) {
            Object[] objs = new Object[6];
            objs[0] = user.getId();
            objs[1] = user.getUsername();
            objs[2] = user.getPassword();
            objs[3] = user.getPhone();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String create = simpleDateFormat.format(user.getCreated());
            objs[4] =create ;
            String updated = simpleDateFormat.format(user.getUpdated());
            objs[5] = updated;
            dataList.add(objs);
        }
        // 创建ExportExcel对象
        ExcelUtil excelUtil = new ExcelUtil();

        try{
            String fileName= new String("UserInfo.xlsx".getBytes("UTF-8"),"iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title,rowsName,dataList,fileName,response);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("get")
    public void get(HttpServletRequest request, HttpServletResponse response){
        //获取后台数据
        List<ContentCategory> cate = contentCategoryService.findAll();
        // 定义表的标题
        String title = "Cate数据";
        //定义表的列名
        String[] rowsName = new String[] { "id", "name"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (ContentCategory category : cate) {
            Object[] objs = new Object[2];
            objs[0] = category.getId();
            objs[1] = category.getName();
            dataList.add(objs);
        }
        // 创建ExportExcel对象
        ExcelUtil excelUtil = new ExcelUtil();

        try{
            String fileName= new String("CateInfo.xlsx".getBytes("UTF-8"),"iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title,rowsName,dataList,fileName,response);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("goods")
    public void getGoods(HttpServletRequest request, HttpServletResponse response){
        //获取后台数据
        List<Goods> goods = goodsService.findAllGoods();
        // 定义表的标题
        String title = "goods数据";
        //定义表的列名
        String[] rowsName = new String[] { "id", "sellerId","goodsName","status","brandId","caption","cate1","cate2","cate3","price","templateId"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<Object[]>();
        for (Goods good : goods) {
            Object[] objs = new Object[11];
            objs[0] = good.getId();
            objs[1] = good.getSellerId();
            objs[2] = good.getGoodsName();
            objs[3] = good.getAuditStatus();
            objs[4] = good.getBrandId();
            objs[5] = good.getCaption();
            objs[6] = good.getCategory1Id();
            objs[7] = good.getCategory2Id();
            objs[8] = good.getCategory3Id();
            objs[9] = good.getPrice();
            objs[10] = good.getTypeTemplateId();
            dataList.add(objs);
        }
        // 创建ExportExcel对象
        ExcelUtil excelUtil = new ExcelUtil();

        try{
            String fileName= new String("goodsInfo.xlsx".getBytes("UTF-8"),"iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title,rowsName,dataList,fileName,response);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("importData")
    public void importData(){
        System.out.println("11111111111");
    }

}
