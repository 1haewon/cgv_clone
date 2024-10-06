package com.example.CGV_clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.CGV_clone", "controller", "service", "config", "repository"})  // 중복된 service 패키지 제거
@EnableJpaRepositories(basePackages = {"com.example.CGV_clone", "repository"})
@EntityScan(basePackages = "domain")
public class CgvCloneApplication {
	public static void main(String[] args) {
		SpringApplication.run(CgvCloneApplication.class, args);
	}
}
