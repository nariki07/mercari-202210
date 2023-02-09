package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.controller.InsertCategoryController;

@SpringBootApplication
public class MercariInsertApplication {
	
	@Autowired
	InsertCategoryController insertCategoryController;
	
	public static void main(String[] args) {
		SpringApplication.run(MercariInsertApplication.class, args);
	}

}
