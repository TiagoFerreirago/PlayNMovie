package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public MovieNotFoundException() {
		super("The requested movie was not found in the system.");
	}
	
	public MovieNotFoundException(String message) {
		super(message);
	}

}
