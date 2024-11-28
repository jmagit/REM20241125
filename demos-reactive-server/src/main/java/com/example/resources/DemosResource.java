package com.example.resources;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.domains.entities.Actor;
import com.example.repositories.ActoresRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/demos")
public class DemosResource {
	@GetMapping("/saluda/{id}")
	public String saluda(@PathVariable String id, @RequestParam(required = false, defaultValue = "Hola") String tipo, 
			@RequestHeader("Accept-Language") String idioma) {
		return tipo + " " + id + " en " + idioma;
	}
	@GetMapping("/datos/simple")
	public Mono<Actor> dato() {
		return Mono.fromSupplier(() -> new Actor(0, "Pepito", "Grillo"));
	}
	@GetMapping("/datos/multiples")
	public Flux<Actor> datos() {
		return Flux.just(new Actor(0, "Pepito", "Grillo"), new Actor(1, "Carmelo", "Coton"));
	}
	
	@GetMapping(path = "/datos/cabeceras")
	public Flux<Entry<String,List<String>>> cotilla(ServerHttpRequest request) {
		return Flux.fromIterable(request.getHeaders().entrySet());
	}
	
	@Autowired
	ActoresRepository dao;
	
	@GetMapping("/db/actores")
	public Flux<Actor> actores() {
//		dao.findById(1);
//		dao.save( new Actor(0, "Pepito", "Grillo"));
//		dao.deleteById(201);
		return dao.findAll();
	}
	@GetMapping("/db/lento")
	public Mono<List<Actor>> lento() {
		return dao.findAll().collectList();
	}
	@GetMapping("/db/lento/{id}")
	public Mono<Actor> lento(@PathVariable int id) {
		return dao.findById(id).delayElement(Duration.ofSeconds(1));
	}
	@GetMapping("/db/rapido/{id}")
	public Mono<Actor> rapido(@PathVariable int id) {
		return dao.findById(id);
	}

}
