package de.bsi.restdemo.validation;

import java.util.Map;
import java.util.TreeMap;

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
		Map<String, String> error = new TreeMap<>();
		error.put(ERROR_TYPE, "Invalid input data");
		error.put(ERROR_MSG, ex.getLocalizedMessage());
		return ResponseEntity.status(400).body(error);
	}
	
	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handleException(Exception ex) {
		Map<String, String> error = new TreeMap<>();
		error.put(ERROR_TYPE, "Unknown & unexpected exception poped up.");
		error.put(ERROR_MSG, ex.getLocalizedMessage());
		return ResponseEntity.status(500).body(error);
	}
}
