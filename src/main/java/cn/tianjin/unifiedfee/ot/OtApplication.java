package cn.tianjin.unifiedfee.ot;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import cn.tianjin.unifiedfee.ot.logvisit.LogVisitListener;

@SpringBootApplication
@EnableFeignClients({"cn.taiji","cn.tianjin.unifiedfee"})
@ComponentScan({"cn.taiji","cn.tianjin.unifiedfee"})
@MapperScan("cn.tianjin.unifiedfee")
@EnableAutoConfiguration
@ServletComponentScan
@Configuration
public class OtApplication {
    public static void main(String[] args) {
        SpringApplication.run(OtApplication.class, args);
        System.out.println("=应用启动====================================================");
        //数据收集器
        LogVisitListener.begin();
    }

    /*
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("20240KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("202400KB");
        return factory.createMultipartConfig();
    }
    */
}