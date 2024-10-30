package com.keepcoding.api.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.keepcoding.api.entity.Customer;
import com.keepcoding.api.entity.Region;
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
			editCustomer.setRegion(customer.getRegion());
			
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
			
			service.customerDelete(id);
			String imagenNameBefore = customerDrop.getImage();
			if(imagenNameBefore != null && imagenNameBefore.length()>0) {
				Path routeBefore = Paths.get("uploads").resolve(imagenNameBefore).toAbsolutePath();
				File fileBefore = routeBefore.toFile();
				
				if(fileBefore.exists() && fileBefore.canRead()) {
					fileBefore.delete();
				}
			}
			
		} catch (DataAccessException e) {
			
			response.put("mensaje", "Error al realizar DELETE en base de datos");
			response.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);			
		}
		
		response.put("mensaje", "Customer has been deleted successfully!");
		response.put("customer", customerDrop);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/customers/regions")
	public ResponseEntity<?> listRegions(){
		
		List<Region> list = service.AllRegions();
		Map<String, String> response = new HashMap<>();
		
		if(list.size()>0) {
			return ResponseEntity.ok(service.AllRegions());
		}else {
			response.put("mensaje", "No hay registros de regiones en esta base de datos actualmente");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}
	
	@PostMapping("/customer/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id){		
		Map<String, Object> response = new HashMap<>();
		
		Customer customer = service.customerById(id);
		if(customer == null) {
			response.put("Mensaje", "No register with id:"+id);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		if(file.isEmpty()) {
			response.put("Mensaje", "File not loaded");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		
		String imagenNameBefore = customer.getImage();
		if(imagenNameBefore != null && imagenNameBefore.length()>0) {
			Path routeBefore = Paths.get("uploads").resolve(imagenNameBefore).toAbsolutePath();
			File fileBefore = routeBefore.toFile();
			
			if(fileBefore.exists() && fileBefore.canRead()) {
				fileBefore.delete();
			}
		}
		
		// Asignación nombre archivo
		// String fileName = file.getOriginalFilename();
		String fileName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename().replace(" ", "");
		// Asignación de path de ruta donde guardamos el archivo
		Path fileRoute =Paths.get("uploads").resolve(fileName).toAbsolutePath();
		
		try {
			
			// Esto es el código que guarda el archivo y asigna el objeto ruta archivo cargado.
			Files.copy(file.getInputStream(), fileRoute);
			
		} catch (IOException e) {
			
			response.put("mensaje", "Error loading imagen to customer");
			response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);			
		}
		
		// Se guarda en BBDD customer el registro de nombre de archivo
		
		customer.setImage(fileName);
		
		service.customerSave(customer);
		
		response.put("mensaje", "Customer has been updated successfully with image: "+fileName);
		response.put("customer", customer);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
		
	}
}
