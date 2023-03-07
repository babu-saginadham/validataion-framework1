package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.controller")
public class Application {

	public static void main(String[] args) {
		//tomcat run
		SpringApplication.run(Application.class, args);
	}
}
