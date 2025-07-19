package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ReviewNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public ReviewNotFoundException() {
		super("The review was not found!");
	}
	
	public ReviewNotFoundException(String msg) {
		super(msg);
	}

}
