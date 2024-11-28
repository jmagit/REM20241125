package com.example;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import com.example.resources.ActoresHandler;

@Configuration
@EnableWebFlux
public class ApiRouterConfigurer implements WebFluxConfigurer {
	public RouterFunction<?> routerPerson(ActoresHandler handler) {
		return RouterFunctions.route().path("/actores/v2", ops -> ops
				.GET("/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getOne)
				.GET(RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getAll)
				.POST("", RequestPredicates.contentType(MediaType.APPLICATION_JSON), handler::create)
				.PUT("/{id}", RequestPredicates.contentType(MediaType.APPLICATION_JSON), handler::update)
				.DELETE("/{id}", handler::delete)
				).build();
	}
}
