package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomizedBadRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CustomizedBadRequestException() {
		super("Invalid request: check the data provided.");
	}
	
	public CustomizedBadRequestException(String message) {
		super(message);
	}

}
