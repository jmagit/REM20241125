package com.example.domains.contracts.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.domains.entities.Contacto;

import reactor.core.publisher.Flux;

public interface ContactosRepository extends ReactiveMongoRepository<Contacto, String> {
	Flux<Contacto> findByConflictivoTrue();
//	<T> Page<T> searchByNombreContainsOrderByNombreAsc(String fragmento, Pageable pageable, Class<T> tipo);
	<T> Flux<T> searchTop20ByNombreContainsOrderByNombreAsc(String fragmento, Class<T> tipo);
	<T> Flux<T> searchByNombreContainsOrderByNombreAsc(String fragmento, Pageable pageable, Class<T> tipo);
	<T> Flux<T> findAllBy(Class<T> tipo);
}
