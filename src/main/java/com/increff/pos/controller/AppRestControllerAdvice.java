package com.increff.pos.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.increff.pos.model.ErrorMessage;
import com.increff.pos.service.ApiException;

@RestControllerAdvice
public class AppRestControllerAdvice {

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorMessage handle(ApiException e) {
		ErrorMessage data = new ErrorMessage();
		data.setMessage(e.getMessage());
		return data;
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage handle(DataIntegrityViolationException e) {
		ErrorMessage data = new ErrorMessage();
		data.setMessage("Cannot perform operation. Foreign key constraints are violated");
		return data;
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorMessage handle(Throwable e) {
		ErrorMessage data = new ErrorMessage();
		data.setMessage("An unknown error has occurred - " + e.getMessage());
		return data;
	}
}