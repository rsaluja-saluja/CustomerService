package com.example.customerservice.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customerdetails")
public class Customer {

	@Id
	@GeneratedValue
	private Integer customerId;
	
	@Embedded
	@ElementCollection
	private List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();

	public Customer() {
		super();
	}

	public Customer(List<PhoneNumber> phoneNumbers) {
		super();
		this.phoneNumbers = phoneNumbers;
	}
	

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", phoneNumbers=" + phoneNumbers + "]";
	}

	
}
