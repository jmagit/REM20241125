package com.example;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.exceptions.BadRequestException;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class ApiExceptionHandler {
	private final Log log = LogFactory.getLog(getClass().getName());

	@ExceptionHandler({ NotFoundException.class })
	public Mono<ProblemDetail> notFoundRequest(Exception exception) {
		return Mono.just(ProblemDetail.forStatus(HttpStatus.NOT_FOUND));
	}

	@ExceptionHandler({ BadRequestException.class, DuplicateKeyException.class, HttpMessageNotReadableException.class })
	public Mono<ProblemDetail> badRequest(Exception exception) {
		log.error("Bad Request exception", exception);
		return Mono.just(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage()));
	}

	@ExceptionHandler({ InvalidDataException.class, MethodArgumentNotValidException.class })
	public Mono<ProblemDetail>  invalidData(Exception exception) {
		log.error("Invalid Data exception", exception);
		return Mono.create(sink -> {
			var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Datos invalidos");
			if (exception instanceof InvalidDataException ex) {
				problem.setProperty("errors", ex.hasErrors() ? ex.getErrors() : exception.getMessage());
			} else if (exception instanceof BindException ex && ex.hasFieldErrors()) {
				var errors = new HashMap<String, String>();
				ex.getFieldErrors().forEach(item -> errors.put(item.getField(), item.getDefaultMessage()));
				problem.setProperty("errors", errors);
			}
			sink.success(problem);
		});
	}

//	@ExceptionHandler({ org.springframework.security.authorization.AuthorizationDeniedException.class })
//	public Mono<ProblemDetail>  AuthorizationDeniedRequest(Exception exception) {
//		log.error(exception.getMessage(), exception);
//		return Mono.just(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage()));
//	}


	@ExceptionHandler({ Exception.class })
	public Mono<ProblemDetail>  unknow(Exception exception) {
		log.error("Unhandled exception", exception);
		return Mono.just(ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
	}

}
