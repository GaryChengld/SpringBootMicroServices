package com.simplecinema.service.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MovieServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(MovieServiceApp.class, args);
	}

}
