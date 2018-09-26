package cn.tianjin.unifiedfee.ot.util;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.taiji.file.service.FileObjectService;

@Configuration
public class FileConfig {
    @Autowired
    private FileObjectService fileObjectService;

    public void setBucketPolicy() {
        try {
            // 设置为所有目录公共读
            fileObjectService.setReadOnly(null);
            // 设置image/目录公共读
            // fileObjectService.setReadOnly("ot/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个数据大小
        factory.setMaxFileSize("10240KB"); // KB,MB
        /// 总上传数据大小
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }
}
