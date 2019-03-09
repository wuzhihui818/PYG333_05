package cn.itcast.core.util;

import cn.itcast.core.common.Common;
import cn.itcast.core.pojo.ad.ContentCategory;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.template.TypeTemplate;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadExcel {
        private InputStream is;
        private HSSFWorkbook hssfWorkbook;

    public List<Brand> readBrand() throws IOException {
        is = new FileInputStream(Common.Brand_PATH);
        hssfWorkbook = new HSSFWorkbook(is);
        Brand brand = null;
        List<Brand> list = new ArrayList<Brand>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    brand=new Brand();
                    HSSFCell id = hssfRow.getCell(0);
                    HSSFCell name = hssfRow.getCell(1);
                    HSSFCell firstChar = hssfRow.getCell(2);
                    HSSFCell status = hssfRow.getCell(3);
                    Float aFloat = Float.valueOf(getValue(id));
                    long ID = aFloat.longValue();
                    brand.setId(ID);
                    brand.setName(getValue(name));
                    /*char[] chars = getValue(firstChar).toCharArray();*/
                    brand.setFirstChar(String.valueOf(getValue(firstChar)));
                    char[] statusArray = getValue(status).toCharArray();
                    brand.setStatus(String.valueOf(statusArray[0]));
                    list.add(brand);
                }
            }
        }
        is.close();
        return list;
    }

    public List<Specification> readSpec() throws IOException {
        is = new FileInputStream(Common.Spec_PATH);
        hssfWorkbook = new HSSFWorkbook(is);
        Specification specification = null;
        List<Specification> list = new ArrayList<Specification>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    specification =new Specification();
                    HSSFCell id = hssfRow.getCell(0);
                    HSSFCell specName = hssfRow.getCell(1);
                    HSSFCell status = hssfRow.getCell(2);
                    Float aFloat = Float.valueOf(getValue(id));
                    specification.setSpecName(getValue(specName));
                    char[] statusArray = getValue(status).toCharArray();
                    specification.setStatus(String.valueOf(statusArray[0]));
                    list.add(specification);
                }
            }
        }
        is.close();
        return list;
    }

    public List<ContentCategory> readCategory() throws IOException {
        is = new FileInputStream(Common.Cate_PATH);
        hssfWorkbook = new HSSFWorkbook(is);
        ContentCategory category = null;
        List<ContentCategory> list = new ArrayList<ContentCategory>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    category=new ContentCategory();
                    HSSFCell id = hssfRow.getCell(0);
                    HSSFCell name = hssfRow.getCell(1);
                    category.setName(getValue(name));
                    list.add(category);
                }
            }
        }
        is.close();
        return list;
    }

    public List<TypeTemplate> readTemp() throws IOException {
        is = new FileInputStream(Common.Temp_PATH);
        hssfWorkbook = new HSSFWorkbook(is);
        TypeTemplate typeTemplate=null;
        List<TypeTemplate> list = new ArrayList<TypeTemplate>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            // 循环行Row
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    typeTemplate=new TypeTemplate();
                    HSSFCell id = hssfRow.getCell(0);
                    HSSFCell name = hssfRow.getCell(1);
                    HSSFCell specIds = hssfRow.getCell(2);
                    HSSFCell brandIds = hssfRow.getCell(3);
                    HSSFCell items = hssfRow.getCell(4);
                    HSSFCell status = hssfRow.getCell(3);
                    typeTemplate.setName(getValue(name));
                    //TODO 数据处理
                    String specIdStr = getValue(specIds);
                    typeTemplate.setSpecIds(specIdStr);
                    typeTemplate.setBrandIds(getValue(brandIds));
                    typeTemplate.setCustomAttributeItems(getValue(items));
                    char[] statusArray = getValue(status).toCharArray();
                    typeTemplate.setStatus(String.valueOf(statusArray[0]));
                    list.add(typeTemplate);
                }
            }
        }
        is.close();
        return list;
    }

    @SuppressWarnings("static-access")
    private String getValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(hssfCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }
}
