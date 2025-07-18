package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FavoriteNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public FavoriteNotFoundException() {
		super("The requested favorite was not found in the system.");
	}
	
	public FavoriteNotFoundException(String msg) {
		super(msg);
	}

}
