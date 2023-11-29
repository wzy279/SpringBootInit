package com.wzyy.springbootinit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Value;

/**
 * 主类（项目启动入口）
 *
 */
@SpringBootApplication()
@MapperScan("com.wzyy.springbootinit.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {

    @Value("${server.port}")
    private static String servePrort;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
        System.out.println("系统启动成功！访问http://localhost:"+servePrort+ "/");

    }

}
