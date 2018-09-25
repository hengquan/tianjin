package cn.tianjin.unifiedfee.ot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableFeignClients({"cn.taiji","cn.tianjin.unifiedfee"})
@ComponentScan({"cn.taiji","cn.tianjin.unifiedfee"})
@MapperScan("cn.tianjin.unifiedfee.ot.mapper")
@EnableAutoConfiguration
@ServletComponentScan
public class OtApplication {
	public static void main(String[] args) {
		SpringApplication.run(OtApplication.class, args);
	}
}