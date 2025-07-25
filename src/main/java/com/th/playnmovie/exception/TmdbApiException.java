package com.th.playnmovie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class TmdbApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public TmdbApiException(String message, Throwable cause) {
		 
		super(message, cause);
	}

}
