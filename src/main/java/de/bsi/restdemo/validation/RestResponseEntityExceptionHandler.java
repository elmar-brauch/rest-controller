package de.bsi.restdemo.validation;

import java.util.Map;
import java.util.TreeMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
	
	private static final String ERROR_TYPE = "error-type";
	private static final String ERROR_MSG = "error-message";
	
	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handleException(MethodArgumentNotValidException ex) {
		return buildResponse(HttpStatus.BAD_REQUEST, 
				"Invalid input data", ex.getLocalizedMessage());
	}
	
	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handleException(IllegalArgumentException ex) {
		return buildResponse(HttpStatus.BAD_REQUEST, 
				"Invalid input data", ex.getLocalizedMessage());
	}
	
	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handleException(Exception ex) {
		return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, 
				"Unknown & unexpected exception", ex.getLocalizedMessage());
	}
	
	private ResponseEntity<Map<String, String>> buildResponse(HttpStatus httpCode, String errorType, String errorMessage) {
		Map<String, String> error = new TreeMap<>();
		error.put(ERROR_TYPE, errorType);
		error.put(ERROR_MSG, errorMessage);
		return ResponseEntity.status(httpCode).body(error);
	}
}
