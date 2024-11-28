package com.example.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.domains.entities.Actor;

import reactor.core.publisher.Flux;

public interface ActoresRepository extends ReactiveCrudRepository<Actor, Integer>  {
	Flux<Actor> searchByFirstNameContainsIgnoreCase(String nombre);
	Flux<Actor> searchByLastNameContainsIgnoreCase(String apellidos);
	Flux<Actor> searchByFirstNameContainsOrLastNameContainsAllIgnoreCase(String nombre, String apellidos);
	Flux<Actor> searchByFirstNameContainsAndLastNameContainsAllIgnoreCase(String nombre, String apellidos);
}
