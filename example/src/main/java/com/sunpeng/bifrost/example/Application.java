package com.sunpeng.bifrost.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 
 * @author sunpeng
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan({ "com.sunpeng.bifrost", "com.sunpeng.bifrost.limiter" })
@EnableWebMvc
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
