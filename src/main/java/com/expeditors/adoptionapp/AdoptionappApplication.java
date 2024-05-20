package com.expeditors.adoptionapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class AdoptionappApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdoptionappApplication.class, args);
	}

}
