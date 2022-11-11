package com.project.numble;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NumbleApplication {

	public static void main(String[] args) {
		SpringApplication.run(NumbleApplication.class, args);
	}

}
