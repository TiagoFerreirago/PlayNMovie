package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReviewAlreadyExistsException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public ReviewAlreadyExistsException() {
		super("A review was found with these specifications!");
	}
	
	public ReviewAlreadyExistsException(String message) {
		super(message);
	}

}
