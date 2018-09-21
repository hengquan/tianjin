package cn.tianjin.unifiedfee.ot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.Map;

@Configuration
public class AfMvcConfig implements WebMvcConfigurer {
    @Autowired
    private FreeMarkerViewResolver freeMarkerViewResolver;

    @Value("${ot-server.prefix}")
    private String prefix;

    /**
     * freemarker 全局变量管理
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        Map<String, Object> map = freeMarkerViewResolver.getAttributesMap();
        // 网关网址前缀
        map.put("gateway", "/"+prefix);
        registry.viewResolver(freeMarkerViewResolver);
    }


    /**
     * 自定义异常页
     */
    @Bean
//    @Profile("dev")
    public ConfigurableServletWebServerFactory containerCustomizer() {
//        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
//        ErrorPage error401Page = new ErrorPage(HttpStatus.FORBIDDEN, "/403");
//        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
//        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
//        factory.addErrorPages(error401Page, error404Page, error500Page);
        return factory;
    }
}
