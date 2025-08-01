package com.example.recipeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
    com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration.class,
    com.google.cloud.spring.autoconfigure.firestore.GcpFirestoreAutoConfiguration.class
})
public class RecipeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeAppApplication.class, args);
	}

}
