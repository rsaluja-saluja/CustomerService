package com.example.customerservice.resource;

import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerList;
import com.example.customerservice.service.CustomerService;

@Validated
@RestController
public class CustomerController {

	@Autowired
	CustomerService custService;

	@GetMapping("/customers/phonenumbers")
	public CustomerList getAllCustPhoneNumbers(
			@RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "Customer Id must be greater than or equal to 1") Integer customerIdToFetchFrom) {
		return custService.getAllPhoneNumbers(customerIdToFetchFrom);
	}

	@GetMapping("/customers/{id}/phonenumbers")
	public Customer getPhoneNumbersById(
			@Min(value = 1, message = "Customer id must be greater than or equal to 1") @PathVariable("id") Integer id) {
		return custService.getPhoneNumberById(id);
	}
}
