package cn.tianjin.unifiedfee.ot.controller;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.taiji.file.service.FileObjectService;
import cn.tianjin.unifiedfee.ot.entity.CommArchive;
import cn.tianjin.unifiedfee.ot.service.CommArchiveService;
import cn.tianjin.unifiedfee.ot.util.HttpPush;

@Controller
@RequestMapping("/file")
public class FileController {
    // 文件管理服务
    @Autowired
    private FileObjectService fileObjectService;
    @Autowired
    private CommArchiveService commArchiveService;
    // 服务器地址
    @Value("${taiji.file.manage.download-endpoint}")
    private String downloadEndpoint;

    // 存储地址
    public final String FILEPATH = "/ot/image/";
    // 文件类型
    public final String FILETYPE = "image/jpg";

    @RequestMapping("/upload")
    @ResponseBody
    public Map<String, Object> upload(CommArchive commArchive, MultipartFile[] files, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpPush.responseInfo(response);// 跨域
        //附件表ID
        String commArchiveId = "";
        System.out.println("------------进入上传文件---------------");
        Map<String, Object> map = new HashMap<String, Object>();
        String fileName = "";
        String fileUrl = "";
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (file != null && file.getSize() > 0) {
                    InputStream stream = file.getInputStream();
                    fileName = file.getOriginalFilename();
                    // 当前时间戳
                    long dateTime = new Date().getTime();
                    String uploadFileName = dateTime + fileName;
                    String objectName = fileObjectService.putObject(FILEPATH, uploadFileName, stream, FILETYPE);
                    System.out.println("--------------------------------");
                    System.out.println(downloadEndpoint + objectName);
                    System.out.println("--------------------------------");
                    fileUrl = downloadEndpoint + objectName;
                    //删除
                    commArchiveService.delObjTableNameAndObjId(commArchive.getObjId(), commArchive.getObjTabname(), commArchive.getArchiveType());
                    // 添加附件
                    commArchive.setFileName(fileName);
                    commArchive.setFilePath("/ot/image/");
                    commArchive.setFileUrl(fileUrl);
                    commArchiveService.insert(commArchive);
                    String id = commArchive.getId();
                    if(StringUtils.isNotEmpty(id))
                        commArchiveId = id;
                }
            }
        }
        map.put("commArchiveId", commArchiveId);
        System.out.println(map.toString());
        return map;
    }

    @RequestMapping("/uploadTemp")
    @ResponseBody
    public Map<String, Object> uploadTemp(CommArchive commArchive, MultipartFile[] files, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpPush.responseInfo(response);// 跨域
        System.out.println("------------进入上传文件---------------");
        Map<String, Object> map = new HashMap<String, Object>();
        String fileName = "";
        String fileUrl = "";
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                if (file != null && file.getSize() > 0) {
                    InputStream stream = file.getInputStream();
                    fileName = file.getOriginalFilename();
                    // 当前时间戳
                    long dateTime = new Date().getTime();
                    String uploadFileName = dateTime + fileName;
                    String objectName = fileObjectService.putObject(FILEPATH, uploadFileName, stream, FILETYPE);
                    System.out.println("--------------------------------");
                    System.out.println(downloadEndpoint + objectName);
                    System.out.println("--------------------------------");
                    fileUrl = downloadEndpoint + objectName;
                }
            }
        }
        map.put("fileName", fileName);
        map.put("fileUrl", fileUrl);
        return map;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Map<String, Object> delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpPush.responseInfo(response);// 跨域
        System.out.println("------------进入删除文件---------------");
        Map<String, Object> map = new HashMap<String, Object>();
        String id = request.getParameter("id") == null ? "" : request.getParameter("id").toString();
        System.out.println("-------------------------------");
        System.out.println(id);
        System.out.println("-------------------------------");
        return map;
    }
}
