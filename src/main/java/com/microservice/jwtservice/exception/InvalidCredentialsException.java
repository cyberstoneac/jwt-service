package com.microservice.jwtservice.exception;

public class InvalidCredentialsException extends Exception {

	private static final long serialVersionUID = -1434350918310383139L;

	public InvalidCredentialsException() {
		super();
	}

	public InvalidCredentialsException(String message) {
		super(message);
	}

	public InvalidCredentialsException(String string, Exception e) {
		super(string, e);
	}

}
