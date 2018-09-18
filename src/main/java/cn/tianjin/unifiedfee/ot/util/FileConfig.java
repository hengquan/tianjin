package cn.tianjin.unifiedfee.ot.util;

import org.springframework.beans.factory.annotation.Autowired;
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
            //fileObjectService.setReadOnly("ot/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
