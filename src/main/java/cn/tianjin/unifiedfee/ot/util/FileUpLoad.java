package cn.tianjin.unifiedfee.ot.util;

import java.io.FileInputStream;
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
public class FileUpLoad {
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

    @RequestMapping("/upLoad123")
    public void fileUp123() throws Exception {
        System.out.println(downloadEndpoint);
        InputStream stream = new FileInputStream("D:\\123.jpg");
        String objectName = fileObjectService.putObject(FILEPATH, "123.jpg", stream, FILETYPE);
        System.out.println(objectName);
        stream.close();
    }

    // 文件上传
    public void fileUp(MultipartFile file) throws Exception {
        if (file != null && file.getSize() > 0) {
            InputStream stream = file.getInputStream();
            String fileName = file.getOriginalFilename();
            String objectName = fileObjectService.putObject(FILEPATH, fileName, stream, FILETYPE);
            System.out.println("--------------------------------");
            System.out.println(downloadEndpoint + objectName);
            System.out.println("--------------------------------");
        }
    }
    // 文件上传
    @RequestMapping("/upLoad")
    @ResponseBody
    public void fileUps( MultipartFile[] files) throws Exception {
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (file != null && file.getSize() > 0) {
                    InputStream stream = file.getInputStream();
                    String fileName = file.getOriginalFilename();
                    String objectName = fileObjectService.putObject(FILEPATH, fileName, stream, FILETYPE);
                    System.out.println("--------------------------------");
                    System.out.println(downloadEndpoint + objectName);
                    System.out.println("--------------------------------");
                }
            }
        }
    }
}
