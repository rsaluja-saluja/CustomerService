package com.example.customerservice.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CustomerList implements Serializable {

	@JsonInclude(Include.NON_NULL)
	private Integer nextCustomerToFetch;
	
	private List<Customer> customers;

	public CustomerList() {
		super();
	}

	public CustomerList(Integer nextCustomerToFetch, List<Customer> customers) {
		super();
		this.nextCustomerToFetch = nextCustomerToFetch;
		this.customers = customers;
	}

	public Integer getNextCustomerToFetch() {
		return nextCustomerToFetch;
	}

	public void setNextCustomerToFetch(Integer nextCustomerToFetch) {
		this.nextCustomerToFetch = nextCustomerToFetch;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	@Override
	public String toString() {
		return "CustomerList [nextCustomerToFetch=" + nextCustomerToFetch + ", customers=" + customers + "]";
	}

	


	
	
}
