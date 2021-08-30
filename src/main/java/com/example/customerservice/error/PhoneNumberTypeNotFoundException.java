package com.example.customerservice.error;

public class PhoneNumberTypeNotFoundException extends RuntimeException {

	public PhoneNumberTypeNotFoundException(Integer id, String type) {
		super("Invalid type: " + type + " for Customer id: " + id);
	}

}