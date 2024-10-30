package com.keepcoding.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.keepcoding.api.entity.User;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Integer>{
	
	Optional<User> findByEmail(String email);

}
