package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MovieAlreadyExistsException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public MovieAlreadyExistsException() {
		super("Movie already exists in the system.");
	}
	
	public MovieAlreadyExistsException(String msg) {
		super(msg);
	}

}
