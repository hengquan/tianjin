package cn.tianjin.unifiedfee.ot.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.taiji.file.service.FileObjectService;

@Controller
@RequestMapping("/file")
public class FileController {
    // 文件管理服务
    @Autowired
    private FileObjectService fileObjectService;
    // 服务器地址
    @Value("${taiji.file.manage.download-endpoint}")
    private String downloadEndpoint;

    // 存储地址
    public final String FILEPATH = "/ot/image/";
    // 文件类型
    public final String FILETYPE = "image/jpg";

    @RequestMapping("/upload")
    @ResponseBody
    public String upload(MultipartFile[] files) throws Exception {
        String fileName = "";
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (file != null && file.getSize() > 0) {
                    InputStream stream = file.getInputStream();
                    fileName = file.getOriginalFilename();
                    String objectName = fileObjectService.putObject(FILEPATH, fileName, stream, FILETYPE);
                    System.out.println("--------------------------------");
                    System.out.println(downloadEndpoint + objectName);
                    System.out.println("--------------------------------");
                }
            }
        }
        return fileName;
    }
}
