package com.keepcoding.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.keepcoding.api.entity.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long>	{

}
