package com.example.customerservice.repo;

import org.springframework.data.repository.CrudRepository;

import com.example.customerservice.model.Customer;

public interface CustomerRepo extends CrudRepository<Customer, Integer> {

}
