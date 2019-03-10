package cn.itcast.core.service;

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
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.util.ReadExcel;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UploadServiceImplCYH implements UploadServiceCYH {

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private SpecificationDao specificationDao;

    @Autowired
    private ContentCategoryDao contentCategoryDao;

    @Autowired
    private TypeTemplateDao typeTemplateDao;

    private ReadExcel xlsMain = new ReadExcel();


    @SuppressWarnings({ "rawtypes" })
    public void saveBrand() throws IOException, SQLException {
        List<Brand> list = xlsMain.readBrand();
        for (Brand brand : list) {
           /* BrandQuery query = new BrandQuery();
            BrandQuery.Criteria criteria = query.createCriteria();
            criteria.andNameEqualTo(brand.getName());
            List<Brand> brands = brandDao.selectByExample(query);*/
            //if (brands.size()==0) {
                brand.setId(null);
                brandDao.insertSelective(brand);
            //}
        }
    }

    public void saveSpec() throws IOException {
        List<Specification> specifications = xlsMain.readSpec();
        for (Specification specification : specifications) {
            /*SpecificationQuery query = new SpecificationQuery();
            SpecificationQuery.Criteria criteria = query.createCriteria();
            criteria.andSpecNameEqualTo(specification.getSpecName());
            List<Specification> list = specificationDao.selectByExample(query);*/
            //if (list.size()==0){
                specificationDao.insertSelective(specification);
            //}
        }
    }
    public void saveCate() throws IOException {
        List<ContentCategory> list = xlsMain.readCategory();
        for (ContentCategory category : list) {
           /* ContentCategoryQuery query = new ContentCategoryQuery();
            ContentCategoryQuery.Criteria criteria = query.createCriteria();
            criteria.andNameEqualTo(category.getName());
            List<ContentCategory> categoryList = contentCategoryDao.selectByExample(query);*/
            //if (categoryList.size()==0){
                contentCategoryDao.insertSelective(category);

            //}
        }
    }

    public void saveTemp() throws IOException {
        List<TypeTemplate> typeTemplates = xlsMain.readTemp();
        for (TypeTemplate typeTemplate : typeTemplates) {
           /* TypeTemplateQuery query = new TypeTemplateQuery();
            TypeTemplateQuery.Criteria criteria = query.createCriteria();
            criteria.andNameEqualTo(typeTemplate.getName());
            List<TypeTemplate> list = typeTemplateDao.selectByExample(query);*/
            //if (list.size()==0){
                typeTemplateDao.insertSelective(typeTemplate);
            //}
        }
    }


}
