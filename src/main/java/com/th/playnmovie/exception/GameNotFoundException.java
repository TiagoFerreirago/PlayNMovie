package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public GameNotFoundException() {
		super("The requested game was not found in the system.");
	}
	
	public GameNotFoundException(String msg) {
		super(msg);
	}

}
