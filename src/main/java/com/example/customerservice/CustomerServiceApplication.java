package com.example.customerservice;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.customerservice.model.Customer;
import com.example.customerservice.repo.CustomerRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
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

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.ant("/customers/**"))
				.apis(RequestHandlerSelectors.basePackage("com.example.customerservice")).build().apiInfo(apiDetails());
	}

	private ApiInfo apiDetails() {
		return new ApiInfo("Customer PhoneNumber API", "API For Customer PhoneNumbers", "1.0", "Terms of Service",
				new Contact("Rajni Saluja", "www.test.com", "rajni.saluja@abc.com"), "License of API",
				"API license URL", Collections.emptyList());
	}
}
