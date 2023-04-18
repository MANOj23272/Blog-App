package com.springboot.blog.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.springboot.blog.dto.LoginDto;
import com.springboot.blog.dto.RegisterDto;
import com.springboot.blog.entity.Role;
import com.springboot.blog.entity.User;
import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authenticationManager;
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	
	public AuthServiceImpl(AuthenticationManager authenticationManager,UserRepository userRepository,RoleRepository roleRepository , PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Override
	public String login(LoginDto loginDto) {
			//Authentication authentication = authenticationManager.authenticate(new  UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()) );
		Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			
			return "user logged in successfully";
	}

	

	@Override
	public String register(RegisterDto registerDto) {
		if(userRepository.existsByUsername(registerDto.getUsername())){
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "username is already exixts");
		}
		if(userRepository.existsByEmail(registerDto.getEmail())) {
			throw new BlogAPIException(HttpStatus.BAD_REQUEST, "email is already exixts");
		}
		User user = new User();
		user.setName(registerDto.getName());
		user.setUsername(registerDto.getUsername());
		
		user.setEmail(registerDto.getEmail());
		user.setPassword( passwordEncoder.encode(registerDto.getPassword()));
		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByname("ROLE_USER").get();
		roles.add(userRole);
		user.setRoles(roles);
		
		userRepository.save(user);
		return "user registered successfully";
	}

}
