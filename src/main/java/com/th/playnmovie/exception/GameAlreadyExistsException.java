package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.th.playnmovie.exception.response.ErrorMessages;

@ResponseStatus(HttpStatus.CONFLICT)
public class GameAlreadyExistsException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public GameAlreadyExistsException() {
		super(ErrorMessages.GAME_ALREADY_EXISTS);	}
	
	public GameAlreadyExistsException(String message) {
		super(message);
	}

}
