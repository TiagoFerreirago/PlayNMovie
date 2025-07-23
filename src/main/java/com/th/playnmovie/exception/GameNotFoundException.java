package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.th.playnmovie.exception.response.ErrorMessages;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFoundException extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
	
	public GameNotFoundException() {
		super(ErrorMessages.GAME_NOT_FOUND);
	}
	
	public GameNotFoundException(String message) {
		super(message);
	}

}
