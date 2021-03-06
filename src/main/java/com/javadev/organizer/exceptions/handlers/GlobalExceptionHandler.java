package com.javadev.organizer.exceptions.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.javadev.organizer.config.SecurityConfig;
import com.javadev.organizer.exceptions.NotDeletedException;
import com.javadev.organizer.exceptions.NotFoundException;
import com.javadev.organizer.exceptions.NotSavedException;
import com.javadev.organizer.exceptions.NotUniqueException;
import com.javadev.organizer.exceptions.NotUpdatedException;
import com.javadev.organizer.exceptions.TokenErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
    @ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	public Error internalServerErrorHandler(Exception exception) {
		logger.error("EXCEPTION | Internal server error [500] -> "+exception.getMessage());
		return new Error(418, "Congratulations. You have found an easter egg");
	}
    
    @ExceptionHandler(TokenErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error tokenExceptionHandler(TokenErrorException tokenErrorException) {
    	logger.info("EXCEPTION | Token error ocurred -> "+tokenErrorException.getMessage());
    	return new Error(400, tokenErrorException.getMessage());
    }
    
	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Error notFoundHandler(NotFoundException notFoundException) {
		logger.info("EXCEPTION | [email="+getCurrentUserEmail()+"]: "+notFoundException.getMessage());
		return new Error(404, notFoundException.getMessage());
	}
	
	@ExceptionHandler(NotUniqueException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Error notUniqueHandler(NotUniqueException notUniqueException) {
		logger.info("EXCEPTION | [email="+getCurrentUserEmail()+"]: "+notUniqueException.getMessage());
		return new Error(409, notUniqueException.getMessage());
	}
	
	@ExceptionHandler(NotSavedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Error notSavedHandler(NotSavedException notSavedException) {
		logger.info("EXCEPTION | [email="+getCurrentUserEmail()+"]: "+notSavedException.getMessage());
		return new Error(409, notSavedException.getMessage());
	}
	
	@ExceptionHandler(NotDeletedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Error notDeletedHandler(NotDeletedException notDeletedException) {
		logger.info("EXCEPTION | [email="+getCurrentUserEmail()+"]: "+notDeletedException.getMessage());
		return new Error(409, notDeletedException.getMessage());
	}
	
	@ExceptionHandler(NotUpdatedException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public Error notUpdatedHandler(NotUpdatedException notUpdatedException) {
		logger.info("EXCEPTION | [email="+getCurrentUserEmail()+"]: "+notUpdatedException.getMessage());
		return new Error(409, notUpdatedException.getMessage());
	}
	
	public String getCurrentUserEmail() {
		return SecurityConfig.getCurrentLoggedInUserEmail();
	}
}
