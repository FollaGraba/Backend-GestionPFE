package com.example.Soutenances;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan(basePackages = "com.example.Soutenances.Entities")  // Assurez-vous que ce package est scanné
public class SoutenancesApplication {

	public static void main(String[] args) {

		SpringApplication.run(SoutenancesApplication.class, args);

	}

}
