package com.keepcoding.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keepcoding.api.entity.Customer;
import com.keepcoding.api.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {
	
	@Autowired
	private CustomerService service;
	
	@GetMapping("/customers")
	public List<Customer> index() {
		return service.allCustomers();
	}
	
	@GetMapping("/customers/{id}")
	public Customer show(@PathVariable Long id) {
		
		return service.customerById(id);
	}

}
