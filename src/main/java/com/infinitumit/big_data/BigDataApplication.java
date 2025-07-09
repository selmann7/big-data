package com.infinitumit.big_data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.infinitumit.big_data.repository")
@EntityScan(basePackages = "com.infinitumit.big_data.entity")
public class BigDataApplication {
	public static void main(String[] args) {
		SpringApplication.run(BigDataApplication.class, args);
	}
}

