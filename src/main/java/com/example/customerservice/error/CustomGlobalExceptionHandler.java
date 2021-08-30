package com.example.customerservice.error;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<CustomErrorResponse> customerNotFoundHandler(Exception ex, WebRequest request) {

		CustomErrorResponse error = new CustomErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setError(HttpStatus.NOT_FOUND.toString());
		error.setDetail(ex.getMessage());

		return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<CustomErrorResponse> handleConstraintViolationException(Exception ex, WebRequest request) {

		CustomErrorResponse error = new CustomErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setError(HttpStatus.BAD_REQUEST.toString());
		error.setDetail(ex.getMessage());

		return new ResponseEntity<CustomErrorResponse>(error, HttpStatus.BAD_REQUEST);
	}

}
