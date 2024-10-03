package com.github.uber_eat_cloneapi1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class UberEatCloneapi1Application {

	public static void main(String[] args) {
		SpringApplication.run(UberEatCloneapi1Application.class, args);
	}

}
