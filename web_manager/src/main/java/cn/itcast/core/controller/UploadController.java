package cn.itcast.core.controller;

import cn.itcast.core.common.FastDFSClient;
import cn.itcast.core.pojo.entity.Result;
import cn.itcast.core.service.UploadServiceCYH;
import cn.itcast.core.util.uploadUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String fileServer;

    @Reference
    private UploadServiceCYH uploadServiceCYH;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) {
        try {
            //创建fastDFS工具类对象
            FastDFSClient fastDFS = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            //上传并返回文件的路径和文件名
            String path = fastDFS.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            //上传后返回fastDFS文件服务器地址+ 上传后的文件路径和文件名
            return new Result(true, fileServer + path);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败!");
        }
    }

    @RequestMapping("/saveExcel")
    public Result saveExcel(MultipartFile file) throws Exception {
        //FastDFSClient fastDFS = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
        String fileName = file.getOriginalFilename();
        System.out.println(fileName);
        uploadUtil.upLoadFile(file);
        try {
            uploadServiceCYH.saveBrand();
         /*   uploadServiceCYH.saveCate();
            uploadServiceCYH.saveSpec();
            uploadServiceCYH.saveTemp();*/

            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"failed");
        }
    }
}
