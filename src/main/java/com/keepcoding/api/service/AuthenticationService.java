package com.keepcoding.api.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.keepcoding.api.dto.LoginUserDto;
import com.keepcoding.api.dto.RegisterUserDto;
import com.keepcoding.api.entity.User;
import com.keepcoding.api.repository.UserRepository;

@Service
public class AuthenticationService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
	public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager) {
		
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
	}
	
	public User signup(RegisterUserDto input) {
		User user = new User();
		user.setFullname(input.getFullName());
		user.setEmail(input.getEmail());
		user.setPassword(input.getPassword());
		
		return userRepository.save(user);
	}
	
	public User authenticate(LoginUserDto input) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
				);
		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}
	
	public List<User> allUsers(){
		return (List<User>) userRepository.findAll();
	}

}
