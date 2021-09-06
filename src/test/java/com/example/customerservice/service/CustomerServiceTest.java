package com.example.customerservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.customerservice.dto.ActiveNumber;
import com.example.customerservice.error.CustomerNotFoundException;
import com.example.customerservice.error.PhoneNumberTypeNotFoundException;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerList;
import com.example.customerservice.repo.CustomerRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest 
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CustomerServiceTest {

	@Autowired
	CustomerService custService;

	//@Autowired
	@MockBean
	CustomerRepo custRepo;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@BeforeEach
	public void setUp() throws IOException {
		byte[] jsonData = Files.readAllBytes(Paths.get("customerPhoneNumbers_test.json"));
		
		List<Customer> list = objectMapper.readValue(jsonData, new TypeReference<ArrayList<Customer>>() {});
		System.out.println("Customers:"+list);
		//custRepo.saveAll(list);
	    Mockito.when(custRepo.findAll())
	      .thenReturn(list);
	    Mockito.when(custRepo.findById(2))
	      .thenReturn(Optional.of(list.get(1)));
	}
	
	@Test
	void testGetCustPhoneNumbersWithoutFetchParam() throws Exception {
		System.out.println("testGetCustPhoneNumbersWithoutFetchParam");
		
		CustomerList custList = custService.getAllPhoneNumbers(1);
		System.out.println("List: "+custList);
		assertEquals(3, custList.getNextCustomerToFetch());
		assertEquals(2, custList.getCustomers().size());
		assertEquals(1, custList.getCustomers().get(0).getCustomerId());
		assertEquals(2, custList.getCustomers().get(1).getCustomerId());

	}
	@Test
	void testGetCustPhoneNumbersWithFetchParam() throws Exception {

		CustomerList custList = custService.getAllPhoneNumbers(2);
		assertEquals(4, custList.getNextCustomerToFetch());
		assertEquals(2, custList.getCustomers().size());
		assertEquals(2, custList.getCustomers().get(0).getCustomerId());
		assertEquals(3, custList.getCustomers().get(1).getCustomerId());

		CustomerList custList1 = custService.getAllPhoneNumbers(6);
		assertEquals(null, custList1.getNextCustomerToFetch());
		assertEquals(1, custList1.getCustomers().size());
		assertEquals(6, custList1.getCustomers().get(0).getCustomerId());

	}
	@Test
	void testGetCustPhoneNumbersCustNotExist() throws Exception {
		System.out.println("testGetCustPhoneNumbersWithoutFetchParam");
		
		CustomerList custList = custService.getAllPhoneNumbers(8);
		assertEquals(0, custList.getCustomers().size());
	}
	
	@Test
	void testGetPhoneNumbersByIdWithCustNotFound() throws Exception {

		Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
			custService.getPhoneNumberById(9);
	    });

	    String expectedMessage = "Customer not found for requested id";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));	

	}

	@Test
	void testGetPhoneNumbersById() throws Exception {

		Customer cust = custService.getPhoneNumberById(2);
	
		assertEquals(2, cust.getCustomerId());
		assertEquals(3, cust.getPhoneNumbers().size());

	}
	
	@Test
	void testActivatePhoneNumber() throws Exception {

		ActiveNumber active = new ActiveNumber("Home", true);

		Customer cust = custService.activatePhoneNumber(2, active);
		
		assertEquals(2, cust.getCustomerId());
		assertTrue(cust.getPhoneNumbers().stream().filter(num -> num.getType().equals(active.getType()))
				.findFirst().get().getActivate());

	}

	@Test
	void testDeActivatePhoneNumber() throws Exception {

		ActiveNumber active = new ActiveNumber("Work", false);

		Customer cust = custService.activatePhoneNumber(2, active);

		assertEquals(2, cust.getCustomerId());
		assertFalse(cust.getPhoneNumbers().stream().filter(num -> num.getType().equals(active.getType()))
				.findFirst().get().getActivate());

	}

	@Test
	void testActivateAllPhoneNumber() throws Exception {

		ActiveNumber active = new ActiveNumber("All", true);

		Customer cust = custService.activatePhoneNumber(2, active);

		assertEquals(2, cust.getCustomerId());
		assertTrue(cust.getPhoneNumbers().stream().allMatch(num -> num.getActivate()));

	}

	@Test
	void testDeActivateAllPhoneNumber() throws Exception {

		ActiveNumber active = new ActiveNumber("All", false);

		Customer cust = custService.activatePhoneNumber(2, active);
		assertEquals(2, cust.getCustomerId());
		assertTrue(cust.getPhoneNumbers().stream().allMatch(num -> !num.getActivate()));

	}

	@Test
	void testActivatePhoneNumberCustNotFound() throws Exception {

		ActiveNumber active = new ActiveNumber("Home", true);

		Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
			custService.activatePhoneNumber(9, active);
	    });

	    String expectedMessage = "Customer not found for requested id";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));	

	}



	@Test
	void testActivatePhoneNumberWithInvalidType() throws Exception {

		ActiveNumber active = new ActiveNumber("test", true);
		
		Exception exception = assertThrows(PhoneNumberTypeNotFoundException.class, () -> {
			custService.activatePhoneNumber(2, active);
	    });

	    String expectedMessage = "Invalid type: test for Customer id:";
	    String actualMessage = exception.getMessage();

	    assertTrue(actualMessage.contains(expectedMessage));			

	}

}
