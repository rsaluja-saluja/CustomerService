package com.example.customerservice.error;

public class CustomerNotFoundException extends RuntimeException {

	public CustomerNotFoundException(Integer id) {
		super("Customer not found for requested id: " + id);
	}

}