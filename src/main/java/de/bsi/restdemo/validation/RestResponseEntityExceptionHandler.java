package de.bsi.restdemo.validation;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {
	
	private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
			MethodArgumentNotValidException.class, BAD_REQUEST, 
			IllegalArgumentException.class, BAD_REQUEST,
			NoSuchElementException.class, NOT_FOUND);
	
	@ExceptionHandler
	public ResponseEntity<Map<String, String>> handleException(Exception ex) throws Exception {
		HttpStatus httpCode = EXCEPTION_STATUS_MAP.get(ex.getClass());
		if (httpCode == null)
			throw ex;
		Map<String, String> error = new TreeMap<>();
		error.put("error-type", ex.getLocalizedMessage());
		error.put("error-message", httpCode.getReasonPhrase());
		return ResponseEntity.status(httpCode).body(error);
	}
}
