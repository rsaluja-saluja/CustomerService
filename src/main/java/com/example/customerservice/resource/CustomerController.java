package com.example.customerservice.resource;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.customerservice.dto.ActiveNumber;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerList;
import com.example.customerservice.service.CustomerService;

import io.swagger.annotations.ApiOperation;

@Validated
@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	CustomerService custService;

	@GetMapping("/phonenumbers")
	@ApiOperation(value = "Get phone numbers of all customers", response = CustomerList.class)
	public CustomerList getAllCustPhoneNumbers(
			@RequestParam(required = false, defaultValue = "1") @Min(value = 1, message = "Customer Id must be greater than or equal to 1") Integer customerIdToFetchFrom) {
		return custService.getAllPhoneNumbers(customerIdToFetchFrom);
	}

	@GetMapping("/{id}/phonenumbers")
	@ApiOperation(value = "Get phone numbers of a particular customer", response = Customer.class)
	public Customer getPhoneNumbersById(
			@Min(value = 1, message = "Customer Id must be greater than or equal to 1") @PathVariable("id") Integer id) {
		return custService.getPhoneNumberById(id);
	}

	@PutMapping("/{id}/phonenumbers")
	@ApiOperation(value = "Activate all or a particular phone number of a customer", response = CustomerList.class)
	public Customer activatePhoneNumber(@Valid @RequestBody ActiveNumber number,
			@Min(value = 1, message = "Customer Id must be greater than or equal to 1") @PathVariable("id") Integer id) {
		return custService.activatePhoneNumber(id, number);
	}
}
