package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class GameAlreadyExistsException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public GameAlreadyExistsException() {
		super("Game already exists in the system.");	}
	
	public GameAlreadyExistsException(String msg) {
		super(msg);
	}

}
