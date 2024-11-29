package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsyncKafkaConsumerApplication {
    @Autowired
	EventListener eventListener;
	
	public static void main(String[] args) {
		SpringApplication.run(AsyncKafkaConsumerApplication.class, args);
	}
}
