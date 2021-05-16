package com.zdp.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 实现静态资源的映射
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")  // 映射swagger2
                .addResourceLocations("file:E:\\workspaces\\images\\");  // 映射本地静态资源
        // 访问时只需补全后续的地址即可
        // 访问地址 http://localhost:8088/foodie/faces/200819F9NG5TZ0H0/face-200819F9NG5TZ0H0.png
        // 实际地址 E:\workspaces\images\foodie\faces\200819F9NG5TZ0H0\face-200819F9NG5TZ0H0.png
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
