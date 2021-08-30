package com.example.customerservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerList;
import com.example.customerservice.repo.CustomerRepo;

@Service
public class CustomerService {

	@Autowired
	CustomerRepo custRepo;
	
	@Value("${customerservice.noOfCustomersToSend}")
	Integer noOfCustomers;
	
	public CustomerList getAllPhoneNumbers(Integer custIdToFetchFrom) {
		
		Integer custIdStart = custIdToFetchFrom;
		Integer custIdEnd = custIdStart+noOfCustomers-1;
		
		List<Customer> customers = new ArrayList<>(); 
		custRepo.findAll().forEach(customers::add);
		
		CustomerList custList = new CustomerList();
		List<Customer> custs = customers.stream().filter(cust -> (cust.getCustomerId()>=custIdStart && cust.getCustomerId()<=custIdEnd)) 
													.collect(Collectors.toList());
		custList.setCustomers(custs);
		Integer nextCustomerToFetch = custIdEnd+1;
		if(customers.stream().filter(cust->cust.getCustomerId()==nextCustomerToFetch).count() > 0) {
				custList.setNextCustomerToFetch(nextCustomerToFetch);
		}
		return custList;
	}
		
}
