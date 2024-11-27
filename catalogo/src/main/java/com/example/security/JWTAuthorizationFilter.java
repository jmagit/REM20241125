package com.example.security;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
	private final String HEADER = HttpHeaders.AUTHORIZATION;
	private final String PREFIX = "Bearer ";
	private String secret;
	AuthenticationEventPublisher authenticationEventPublisher;
	AuditEventRepository auditEventRepository;
	
	public JWTAuthorizationFilter(String secret, AuthenticationEventPublisher authenticationEventPublisher, AuditEventRepository auditEventRepository) {
		super();
		this.secret = secret;
		this.authenticationEventPublisher = authenticationEventPublisher;
		this.auditEventRepository = auditEventRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			String authenticationHeader = request.getHeader(HEADER);
			if (authenticationHeader != null && authenticationHeader.startsWith(PREFIX)) {
				KeyFactory kf = KeyFactory.getInstance("RSA");
				X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(secret));
				RSAPublicKey publicKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
				DecodedJWT token = JWT.require(Algorithm.RSA256(publicKey, null)).withIssuer("MicroserviciosJWT")
						.withAudience("authorization").build().verify(authenticationHeader.substring(PREFIX.length()));
				List<GrantedAuthority> authorities = token.getClaim("authorities").asList(String.class).stream()
						.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						token.getClaim("username").asString(), null, authorities);
				SecurityContextHolder.getContext().setAuthentication(auth);
				authenticationEventPublisher.publishAuthenticationSuccess(auth);
				if(auditEventRepository != null) 
					auditEventRepository.add(new AuditEvent(auth.getName(), "AUTHENTICATION_SUCCESS"));
			}
			chain.doFilter(request, response);
		} catch (JWTVerificationException ex) {
//			authenticationEventPublisher.publishAuthenticationFailure(new BadCredentialsException("Invalid."), null);
//			if(auditEventRepository != null) 
//				auditEventRepository.add(new AuditEvent("anonymousUser", "AUTHENTICATION_FAILURE"));
			throw new AccessDeniedException("AUTHENTICATION_FAILURE_1", ex);
//			chain.doFilter(request, response);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
//			throw new AuthorizationServiceException("AUTHENTICATION_FAILURE_2", ex);
			chain.doFilter(request, response);
		}
	}
}
