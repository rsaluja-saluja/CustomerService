package com.example.customerservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.customerservice.dto.ActiveNumber;
import com.example.customerservice.error.CustomerNotFoundException;
import com.example.customerservice.error.PhoneNumberTypeNotFoundException;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerList;
import com.example.customerservice.model.PhoneNumber;
import com.example.customerservice.repo.CustomerRepo;

@Service
public class CustomerService {

	@Autowired
	CustomerRepo custRepo;

	@Value("${customerservice.noOfCustomersToSend}")
	Integer noOfCustomers;

	public CustomerList getAllPhoneNumbers(Integer custIdToFetchFrom) {

		Integer custIdStart = custIdToFetchFrom;
		Integer custIdEnd = custIdStart + noOfCustomers - 1;

		List<Customer> customers = new ArrayList<>();
		custRepo.findAll().forEach(customers::add);

		CustomerList custList = new CustomerList();
		List<Customer> custs = customers.stream()
				.filter(cust -> (cust.getCustomerId() >= custIdStart && cust.getCustomerId() <= custIdEnd))
				.collect(Collectors.toList());
		custList.setCustomers(custs);
		Integer nextCustomerToFetch = custIdEnd + 1;
		if (customers.stream().filter(cust -> cust.getCustomerId() == nextCustomerToFetch).count() > 0) {
			custList.setNextCustomerToFetch(nextCustomerToFetch);
		}
		return custList;
	}

	public Customer getPhoneNumberById(Integer id) {
		Optional<Customer> cust = custRepo.findById(id);
		if (cust.isPresent())
			return cust.get();
		else
			throw new CustomerNotFoundException(id);
	}

	public Customer activatePhoneNumber(Integer id, ActiveNumber number) {
		Optional<Customer> cust = custRepo.findById(id);
		if (cust.isPresent()) {
			List<PhoneNumber> phoneNumbers = cust.get().getPhoneNumbers();
			if (number.getType().equalsIgnoreCase("all")) {
				phoneNumbers.forEach(num -> num.setActivate(number.getActivate()));
			} else {
				Optional<PhoneNumber> PhoneNum = phoneNumbers.stream()
						.filter(num -> num.getType().equalsIgnoreCase(number.getType())).findFirst();
				if (PhoneNum.isPresent()) {
					PhoneNum.get().setActivate(number.getActivate());
				} else
					throw new PhoneNumberTypeNotFoundException(id, number.getType());

			}
			custRepo.save(cust.get());
			return cust.get();
		} else
			throw new CustomerNotFoundException(id);
	}
}
