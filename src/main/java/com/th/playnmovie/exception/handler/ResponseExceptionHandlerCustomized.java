package com.th.playnmovie.exception.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.th.playnmovie.exception.*;
import com.th.playnmovie.exception.response.ResponseException;

@ControllerAdvice
public class ResponseExceptionHandlerCustomized {

    private ResponseEntity<ResponseException> buildResponse(Exception ex, WebRequest request, HttpStatus status) {
        ResponseException response = new ResponseException(
            ex.getMessage(),
            request.getDescription(false),
            new Date()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseException> handleGenericException(Exception ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomizedBadRequestException.class)
    public ResponseEntity<ResponseException> handleBadRequest(CustomizedBadRequestException ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameAlreadyExistsException.class)
    public ResponseEntity<ResponseException> handleGameAlreadyExists(GameAlreadyExistsException ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ResponseException> handleGameNotFound(GameNotFoundException ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovieAlreadyExistsException.class)
    public ResponseEntity<ResponseException> handleMovieAlreadyExists(MovieAlreadyExistsException ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ResponseException> handleMovieNotFound(MovieNotFoundException ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(FavoriteAlreadyExistsException.class)
    public ResponseEntity<ResponseException> handleFavoriteAlreadyExists(FavoriteAlreadyExistsException ex, WebRequest request) {
    	return buildResponse(ex, request, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(FavoriteNotFoundException.class)
    public ResponseEntity<ResponseException> handleFavoriteNotFound(FavoriteNotFoundException ex, WebRequest request) {
    	return buildResponse(ex, request, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public ResponseEntity<ResponseException> handleReviewAlreadyExists(ReviewAlreadyExistsException ex, WebRequest request) {
    	return buildResponse(ex, request, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(ReviewNotFoundException.class)
    public ResponseEntity<ResponseException> handleReviewNotFound(ReviewNotFoundException ex, WebRequest request) {
    	return buildResponse(ex, request, HttpStatus.NOT_FOUND);
    }
}
