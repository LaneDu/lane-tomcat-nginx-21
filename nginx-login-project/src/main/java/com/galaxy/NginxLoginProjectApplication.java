package com.galaxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 打war包要继承SpringBootServletInitializer实现方法configure
 * @author lane
 * @date 2021/5/15 下午4:03
 */
@SpringBootApplication
@EnableCaching
@EnableRedisHttpSession
public class NginxLoginProjectApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(NginxLoginProjectApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(NginxLoginProjectApplication.class, args);
    }

}

