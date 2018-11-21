package com.ntnikka;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages = {"com.ntnikka.modules.*.dao", "com.ntnikka.modules.*.*.dao"})
public class RhAdminApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.sources(this.getClass());
        return super.configure(builder);
    }

    public static void main(String[] args) {
        SpringApplication.run(RhAdminApplication.class, args);
        //指定jre系统属性，允许 特殊符号  | 做入参   详情见 tomcat  HttpParser类
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow", "|");
        System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow", "{}");
    }
}
