package com.aurea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class DeadCodeApplication {
    
    @Bean
    public ThreadPoolTaskExecutor executor() {
        return new ThreadPoolTaskExecutor();
    }

	public static void main(String[] args) {
		SpringApplication.run(DeadCodeApplication.class, args);
	}
}
