package com.example.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UnauthorizedHandler
		implements AccessDeniedHandler, AuthenticationEntryPoint, AuthenticationFailureHandler {

	private void send(HttpServletRequest request, HttpServletResponse response, Exception exception)
			throws IOException, ServletException {
		System.err
				.println(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage()).toString());
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage()).toString());
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
			throws IOException, ServletException {
		send(request, response, exception);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		send(request, response, authException);

	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		send(request, response, exception);

	}
}

//@Component
//public class UnauthorizedHandler implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
//System.err.println(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage()).toString());
//    	response.sendError(
//        		HttpServletResponse.SC_UNAUTHORIZED, 
//        		ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage()).toString()
//        		);
//    }
//}