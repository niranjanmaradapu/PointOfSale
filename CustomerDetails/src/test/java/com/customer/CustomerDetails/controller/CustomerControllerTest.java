package com.customer.CustomerDetails.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.customer.CustomerDetails.customer.CustomerDetails;
import com.customer.CustomerDetails.service.CustomerService;

class CustomerControllerTest {

	@InjectMocks
	CustomerController controller;

	@Mock
	CustomerService service;

	@Mock
	private RestTemplate restTemplate;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

	}

	@Test
	void testSaveCustomerDetails() throws Exception {
		CustomerDetails details = new CustomerDetails();
		details.setName("Guptha");
		details.setGender("Male");
		details.setMobileNumber("9059006755");
		details.setAddress("Hyderabad"); // details.setDob(2021-06-21T18:30:00.000Z);
		details.setGstNumber("GGCHIIEOKD");
		details.setAltMobileNo("9059003782");

		String body = "Customer details saved successfully..";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		ResponseEntity<?> value = new ResponseEntity<>(body, headers, HttpStatus.OK);

		// assertEquals(controller.saveCustomerDetails(details)), new
		// ResponseEntity(body, HttpStatus.OK));

		when(restTemplate.postForEntity("http://localhost:8081/customer/savecustomerdetails", details,
				CustomerDetails.class)).thenReturn(new ResponseEntity(body, HttpStatus.OK));

		// ResponseEntity<?> value = new ResponseEntity<>(body, HttpStatus.OK);
		/*
		 * Mockito.doReturn(new
		 * ResponseEntity<>(body,HttpStatus.OK)).when(service.saveCustomerDetails(
		 * details));
		 */
		// assertEquals(body, value.getBody());

		// when(service.saveCustomerDetails(details)).thenReturn(value.getBody());

	}

	/*
	 * @Autowired private CustomerService service;
	 * 
	 * @MockBean private CustomerDetailsRepo repo;
	 * 
	 * CustomerDetailsEntity details;
	 * 
	 * @Test public void saveCustomerDetails() { details = new
	 * CustomerDetailsEntity(); details.setName("Guptha");
	 * details.setGender("Male"); details.setMobileNumber("9059006755");
	 * details.setAddress("Hyderabad"); // details.setDob(2021-06-21T18:30:00.000Z);
	 * details.setGstNumber("GGCHIIEOKD"); details.setAltMobileNo("9059003782"); //
	 * assertEquals(expected, actual); when(repo.save(details)).thenReturn(details);
	 * }
	 */

}
