package com.keepcoding.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.keepcoding.api.entity.Customer;
import com.keepcoding.api.entity.Region;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>	{
	
	@Query("from Region")
	public List<Region> findAllRegions();

}
