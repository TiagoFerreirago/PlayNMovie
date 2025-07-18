package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FavoriteAlreadyExistsException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public FavoriteAlreadyExistsException() {
		super("Was already a favorite!");
	}
	
	public FavoriteAlreadyExistsException(String msg) {
		super(msg);
	}

}
