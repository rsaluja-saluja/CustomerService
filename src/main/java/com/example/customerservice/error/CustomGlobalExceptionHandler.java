package com.example.customerservice.error;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

	@ExceptionHandler(PhoneNumberTypeNotFoundException.class)
	public ResponseEntity<CustomErrorResponse> customerPhoneNumberTypeNotFoundHandler(Exception ex, WebRequest request) {

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

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", status.value());
		body.put("error", status.toString());

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("details", errors);

		return new ResponseEntity<>(body, headers, status);

	}
}
