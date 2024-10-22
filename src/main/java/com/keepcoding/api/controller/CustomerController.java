package com.keepcoding.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.keepcoding.api.entity.Customer;
import com.keepcoding.api.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {
	
	@Autowired
	private CustomerService service;
	
	@GetMapping("/customers")
	public ResponseEntity<?> index() {
		
		List<Customer> list = service.allCustomers();
		
		Map<String, String> response = new HashMap<>();
		if(list.size()>0) {
			return ResponseEntity.ok(service.allCustomers());
		}else {
			response.put("mensaje", "No hay registros en esta base de datos actualmente");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		// return service.allCustomers();
	}
	
	@GetMapping("/customers/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		
		Customer findCustomer = service.customerById(id);
		Map<String, String> response = new HashMap<>();
		if(findCustomer != null) {
			return ResponseEntity.ok(findCustomer);
		}else {
			response.put("mensaje", "No hay registros con este id:"+id);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
	}
	
	@PostMapping("/customer")
	// @ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
		
		Customer customerNew = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			customerNew = service.customerSave(customer);
			
		} catch (DataAccessException e) {
			
			response.put("mensaje", "Error al realizar INSERT en base de datos");
			response.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		response.put("mensaje", "Customer has been created successfully!");
		response.put("customer", customerNew);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PutMapping("/customer/edit/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
		
		Customer customerUpdate =  null;
		Map<String, Object> response = new HashMap<>();
		
		Customer editCustomer = service.customerById(id);
		
		if(editCustomer == null) {
			response.put("Mensaje", "No existe registro con id:"+id);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		try {
			
			editCustomer.setName(customer.getName());
			editCustomer.setLastname(customer.getLastname());
			editCustomer.setEmail(customer.getEmail());
			editCustomer.setPhone(customer.getPhone());
			editCustomer.setCreatedAt(customer.getCreatedAt());
			
			customerUpdate = service.customerSave(editCustomer);
			
		} catch (DataAccessException e) {
			
			response.put("mensaje", "Error al realizar UPDATE en base de datos");
			response.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			
		}
		
		response.put("mensaje", "Customer has been updated successfully!");
		response.put("customer", customerUpdate);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	
	}
	
	@DeleteMapping("/customer/{id}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
		
		Customer customerDrop = null;
		
		Map<String, Object> response = new HashMap<>();
		
		customerDrop = service.customerById(id);
		if(customerDrop == null) {
			response.put("Mensaje", "No existe registro con id:"+id);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		try {
			
		} catch (DataAccessException e) {
			
			response.put("mensaje", "Error al realizar DELETE en base de datos");
			response.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);			
		}
		
		response.put("mensaje", "Customer has been deleted successfully!");
		response.put("customer", customerDrop);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
