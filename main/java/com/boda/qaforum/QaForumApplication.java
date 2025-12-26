package com.boda.qaforum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.boda.qaforum.model")
@EnableJpaRepositories(basePackages = "com.boda.qaforum.repository")
public class QaForumApplication {

    public static void main(String[] args) {
        SpringApplication.run(QaForumApplication.class, args);
        System.out.println("=========================================");
        System.out.println("问答论坛应用启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("H2控制台: http://localhost:8080/h2-console");
        System.out.println("=========================================");
    }
}