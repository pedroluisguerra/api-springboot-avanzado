package com.keepcoding.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.keepcoding.api.entity.Customer;
import com.keepcoding.api.entity.Region;

public interface CustomerService {
	
	public List<Customer> allCustomers();
	
	public Customer customerById(Long id);
	
	public Customer customerSave(Customer customer);
	
	public void customerDelete(Long id);
	
	public List<Region> AllRegions();

}
