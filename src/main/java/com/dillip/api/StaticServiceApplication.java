package com.dillip.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StaticServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StaticServiceApplication.class, args);
	}

}
