package com.hailey.book.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 이 클래스는 항상 프로젝트 최상단에 위치

@EnableJpaAuditing
@SpringBootApplication
public class Application {
    public static  void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}