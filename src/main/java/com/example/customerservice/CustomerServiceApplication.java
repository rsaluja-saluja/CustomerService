package com.example.customerservice;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.customerservice.model.Customer;
import com.example.customerservice.repo.CustomerRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner customerDemo(CustomerRepo custRepo) {
		return (args) -> {
			byte[] jsonData = Files.readAllBytes(Paths.get("customerPhoneNumbers.json"));

			ObjectMapper objectMapper = new ObjectMapper();
			List<Customer> list = objectMapper.readValue(jsonData, new TypeReference<ArrayList<Customer>>() {
			});
			custRepo.saveAll(list);
		};
	}
}
