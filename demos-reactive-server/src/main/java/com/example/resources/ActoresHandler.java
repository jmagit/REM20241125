package com.example.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.domains.entities.Actor;
import com.example.repositories.ActoresRepository;

import reactor.core.publisher.Mono;

@Service
public class ActoresHandler {

	@Autowired
	private ActoresRepository dao;
	
	public Mono<ServerResponse> getAll(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(dao.findAll(), Actor.class);
	}
	public Mono<ServerResponse> getOne(ServerRequest request) {
		return dao.findById(Integer.parseInt(request.pathVariable("id")))
				.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(item))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
	public Mono<ServerResponse> create(ServerRequest request) {
		return request.bodyToMono(Actor.class)
				.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(dao.save(item), Actor.class));
	}
	public Mono<ServerResponse> update(ServerRequest request) {
		return request.bodyToMono(Actor.class)
				.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(dao.save(item), Actor.class));
	}
	public Mono<ServerResponse> delete(ServerRequest request) {
		return dao.deleteById(Integer.parseInt(request.pathVariable("id")))
				.then(ServerResponse.noContent().build());
	}
}
