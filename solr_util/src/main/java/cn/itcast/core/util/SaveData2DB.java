package cn.itcast.core.util;

import cn.itcast.core.dao.ad.ContentCategoryDao;
import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.specification.SpecificationDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.ad.ContentCategory;
import cn.itcast.core.pojo.ad.ContentCategoryQuery;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Component
public class SaveData2DB {
    @Autowired
    private BrandDao brandDao;

    @Autowired
    private SpecificationDao specificationDao;

    @Autowired
    private ContentCategoryDao contentCategoryDao;

    @Autowired
    private TypeTemplateDao typeTemplateDao;

    private ReadExcel xlsMain = new ReadExcel();

    //1. 创建spring运行环境对象, 加载spring配置文件
    private static ApplicationContext application = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
    //获取当前类对象
    private static SaveData2DB saveData2DB =(SaveData2DB) application.getBean("saveData2DB");

    @SuppressWarnings({ "rawtypes" })
    public void saveBrand() throws IOException, SQLException {
        List<Brand> list = xlsMain.readBrand();
        for (Brand brand : list) {
            BrandQuery query = new BrandQuery();
            BrandQuery.Criteria criteria = query.createCriteria();
            criteria.andNameEqualTo(brand.getName());
            List<Brand> brands = brandDao.selectByExample(query);
            if (brands.size()==0) {
                brand.setId(null);
                brandDao.insertSelective(brand);
            }
        }
    }

    public void saveSpec() throws IOException {
        List<Specification> specifications = xlsMain.readSpec();
        for (Specification specification : specifications) {
            SpecificationQuery query = new SpecificationQuery();
            SpecificationQuery.Criteria criteria = query.createCriteria();
            criteria.andSpecNameEqualTo(specification.getSpecName());
            List<Specification> list = specificationDao.selectByExample(query);
            if (list.size()==0){
                specificationDao.insertSelective(specification);
            }
        }
    }
    public void saveCate() throws IOException {
        List<ContentCategory> list = xlsMain.readCategory();
        for (ContentCategory category : list) {
            ContentCategoryQuery query = new ContentCategoryQuery();
            ContentCategoryQuery.Criteria criteria = query.createCriteria();
            criteria.andNameEqualTo(category.getName());
            List<ContentCategory> categoryList = contentCategoryDao.selectByExample(query);
            if (categoryList.size()==0){
                contentCategoryDao.insertSelective(category);

            }
        }
    }

    public void saveTemp() throws IOException {
        List<TypeTemplate> typeTemplates = xlsMain.readTemp();
        for (TypeTemplate typeTemplate : typeTemplates) {
            TypeTemplateQuery query = new TypeTemplateQuery();
            TypeTemplateQuery.Criteria criteria = query.createCriteria();
            criteria.andNameEqualTo(typeTemplate.getName());
            List<TypeTemplate> list = typeTemplateDao.selectByExample(query);
            if (list.size()==0){
                typeTemplateDao.insertSelective(typeTemplate);
            }
        }
    }


    public static void main(String[] args) throws IOException, SQLException {
        saveData2DB.saveBrand();
        saveData2DB.saveSpec();
        saveData2DB.saveCate();
        saveData2DB.saveTemp();
        System.out.println("end");
    }
}
