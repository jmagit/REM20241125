package com.example;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.example.exceptions.BadRequestException;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@RestControllerAdvice
public class ApiExceptionHandler {
	private final Log log = LogFactory.getLog(getClass().getName());

	@ExceptionHandler({ ErrorResponseException.class, ResponseStatusException.class, HttpRequestMethodNotSupportedException.class })
	public ProblemDetail defaultResponse(ErrorResponse exception) {
		return exception.getBody();
	}

	@ExceptionHandler({ NotFoundException.class })
	public ProblemDetail notFoundRequest(Exception exception) {
		return ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ BadRequestException.class, DuplicateKeyException.class, HttpMessageNotReadableException.class })
	public ProblemDetail badRequest(Exception exception) {
		log.error("Bad Request exception", exception);
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
	}

	@ExceptionHandler({ InvalidDataException.class, MethodArgumentNotValidException.class })
	public ProblemDetail invalidData(Exception exception) {
		log.error("Invalid Data exception", exception);
		var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Datos invalidos");
		if (exception instanceof InvalidDataException ex) {
			problem.setProperty("errors", ex.hasErrors() ? ex.getErrors() : exception.getMessage());
		} else if (exception instanceof BindException ex && ex.hasFieldErrors()) {
			var errors = new HashMap<String, String>();
			ex.getFieldErrors().forEach(item -> errors.put(item.getField(), item.getDefaultMessage()));
			problem.setProperty("errors", errors);
		}
		return problem;
	}

	@ExceptionHandler({ Exception.class })
	public ProblemDetail unknow(Exception exception) {
		log.error("Unhandled exception", exception);
		return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
	}

}
