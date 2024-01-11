package com.abhinav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.abhinav")
public class SpringSecurityLatestApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringSecurityLatestApplication.class, args);
	}

}
