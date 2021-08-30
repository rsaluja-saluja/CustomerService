package com.example.customerservice.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.customerservice.dto.ActiveNumber;
import com.example.customerservice.error.CustomErrorResponse;
import com.example.customerservice.model.Customer;
import com.example.customerservice.model.CustomerList;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CustomerControllerTest {

	@LocalServerPort
	private int port;

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testGetCustPhoneNumbersWithoutFetchParam() throws Exception {

		ResponseEntity<CustomerList> response = this.restTemplate
				.getForEntity("http://localhost:" + port + "/customers/phonenumbers", CustomerList.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(3, response.getBody().getNextCustomerToFetch());
		assertEquals(2, response.getBody().getCustomers().size());
		assertEquals(1, response.getBody().getCustomers().get(0).getCustomerId());
		assertEquals(2, response.getBody().getCustomers().get(1).getCustomerId());

	}

	@Test
	void testGetCustPhoneNumbersWithFetchParam() throws Exception {

		ResponseEntity<CustomerList> response = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/customers/phonenumbers?customerIdToFetchFrom=2", CustomerList.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(4, response.getBody().getNextCustomerToFetch());
		assertEquals(2, response.getBody().getCustomers().size());
		assertEquals(2, response.getBody().getCustomers().get(0).getCustomerId());
		assertEquals(3, response.getBody().getCustomers().get(1).getCustomerId());

		ResponseEntity<CustomerList> response1 = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/customers/phonenumbers?customerIdToFetchFrom=7", CustomerList.class);
		assertEquals(HttpStatus.OK, response1.getStatusCode());
		assertEquals(null, response1.getBody().getNextCustomerToFetch());
		assertEquals(1, response1.getBody().getCustomers().size());
		assertEquals(7, response1.getBody().getCustomers().get(0).getCustomerId());

	}

	@Test
	void testGetCustPhoneNumbersWithInvalidFetchParam() throws Exception {

		ResponseEntity<CustomErrorResponse> response = this.restTemplate.getForEntity(
				"http://localhost:" + port + "/customers/phonenumbers?customerIdToFetchFrom=0",
				CustomErrorResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getDetail().contains("Customer Id must be greater than or equal to 1"));

	}

	@Test
	void testGetPhoneNumbersByIdWithInvalidId() throws Exception {

		ResponseEntity<CustomErrorResponse> response = this.restTemplate
				.getForEntity("http://localhost:" + port + "/customers/0/phonenumbers", CustomErrorResponse.class);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getDetail().contains("Customer Id must be greater than or equal to 1"));

	}

	@Test
	void testGetPhoneNumbersByIdWithCustNotFound() throws Exception {

		ResponseEntity<CustomErrorResponse> response = this.restTemplate
				.getForEntity("http://localhost:" + port + "/customers/9/phonenumbers", CustomErrorResponse.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody().getDetail().contains("Customer not found for requested id"));

	}

	@Test
	void testGetPhoneNumbersById() throws Exception {

		ResponseEntity<Customer> response = this.restTemplate
				.getForEntity("http://localhost:" + port + "/customers/2/phonenumbers", Customer.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().getCustomerId());
		assertEquals(3, response.getBody().getPhoneNumbers().size());

	}

	@Test
	void testActivatePhoneNumber() throws Exception {

		ActiveNumber active = new ActiveNumber("Home", true);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<Customer> response = restTemplate.exchange("/customers/2/phonenumbers", HttpMethod.PUT, entity,
				Customer.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().getCustomerId());

		assertTrue(response.getBody().getPhoneNumbers().stream().filter(num -> num.getType().equals(active.getType()))
				.findFirst().get().getActivate());

	}

	@Test
	void testDeActivatePhoneNumber() throws Exception {

		ActiveNumber active = new ActiveNumber("Work", false);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<Customer> response = restTemplate.exchange("/customers/2/phonenumbers", HttpMethod.PUT, entity,
				Customer.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().getCustomerId());
		assertFalse(response.getBody().getPhoneNumbers().stream().filter(num -> num.getType().equals(active.getType()))
				.findFirst().get().getActivate());

	}

	@Test
	void testActivateAllPhoneNumber() throws Exception {

		ActiveNumber active = new ActiveNumber("All", true);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<Customer> response = restTemplate.exchange("/customers/2/phonenumbers", HttpMethod.PUT, entity,
				Customer.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().getCustomerId());
		assertTrue(response.getBody().getPhoneNumbers().stream().allMatch(num -> num.getActivate()));

	}

	@Test
	void testDeActivateAllPhoneNumber() throws Exception {

		ActiveNumber active = new ActiveNumber("All", false);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<Customer> response = restTemplate.exchange("/customers/2/phonenumbers", HttpMethod.PUT, entity,
				Customer.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().getCustomerId());
		assertTrue(response.getBody().getPhoneNumbers().stream().allMatch(num -> !num.getActivate()));

	}

	@Test
	void testActivatePhoneNumberCustNotFound() throws Exception {

		ActiveNumber active = new ActiveNumber("Home", true);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<CustomErrorResponse> response = restTemplate.exchange("/customers/9/phonenumbers",
				HttpMethod.PUT, entity, CustomErrorResponse.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody().getDetail().contains("Customer not found for requested id"));

	}

	@Test
	void testActivatePhoneNumberWithInvalidId() throws Exception {

		ActiveNumber active = new ActiveNumber("Home", true);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<CustomErrorResponse> response = restTemplate.exchange("/customers/0/phonenumbers",
				HttpMethod.PUT, entity, CustomErrorResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().getDetail().contains("Customer Id must be greater than or equal to 1"));

	}

	@Test
	void testActivatePhoneNumberWithInvalidType() throws Exception {

		ActiveNumber active = new ActiveNumber("test", true);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<CustomErrorResponse> response = restTemplate.exchange("/customers/1/phonenumbers",
				HttpMethod.PUT, entity, CustomErrorResponse.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody().getDetail().contains("Invalid type: test for Customer id:"));

	}

	@Test
	void testActivatePhoneNumberWithTypeNotProvided() throws Exception {

		ActiveNumber active = new ActiveNumber(null, true);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<String> response = restTemplate.exchange("/customers/0/phonenumbers", HttpMethod.PUT, entity,
				String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains("Please provide Phone Number type"));

	}

	@Test
	void testActivatePhoneNumberWithActivateStatusNotProvided() throws Exception {

		ActiveNumber active = new ActiveNumber("Home", null);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<String> response = restTemplate.exchange("/customers/0/phonenumbers", HttpMethod.PUT, entity,
				String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains("Please provide Phone Number Activate status"));

	}

	@Test
	void testActivatePhoneNumberWithActiveInfoNotProvided() throws Exception {

		ActiveNumber active = new ActiveNumber(null, null);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(om.writeValueAsString(active), headers);

		ResponseEntity<String> response = restTemplate.exchange("/customers/0/phonenumbers", HttpMethod.PUT, entity,
				String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertTrue(response.getBody().contains("Please provide Phone Number Activate status"));
		assertTrue(response.getBody().contains("Please provide Phone Number type"));

	}
}
