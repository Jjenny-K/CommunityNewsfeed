package com.handicraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HandicraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(HandicraftApplication.class, args);
	}

}
