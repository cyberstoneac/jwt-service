package com.microservice.jwtservice.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.jwtservice.exception.InvalidCredentialsException;
import com.microservice.jwtservice.model.AuthenticationRequest;
import com.microservice.jwtservice.model.AuthenticationResponse;
import com.microservice.jwtservice.service.UserDetailsServiceExt;
import com.microservice.jwtservice.util.JwtUtil;

@RestController
public class HelloWorldController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private UserDetailsServiceExt userDetailsService;

	@GetMapping("/hello")
	public String firstPage() {
		return "Hello World";
	}

	@PostMapping(value = "/authenticate")
	public ResponseEntity<Object> createAuthenticationToken(
			@RequestBody @Valid AuthenticationRequest authenticationRequest, BindingResult result)
			throws InvalidCredentialsException {

		if (result.hasErrors()) {
			List<String> errors = new ArrayList<>();
			for (ObjectError error : result.getAllErrors()) {
				errors.add(error.getDefaultMessage());
			}
			return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
		}
		
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new InvalidCredentialsException("Incorrect username or password", e);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
