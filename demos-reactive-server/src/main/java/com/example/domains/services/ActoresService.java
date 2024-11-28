package com.example.domains.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.domains.entities.Actor;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;
import com.example.repositories.ActoresRepository;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ActoresService {
	@Autowired
	ActoresRepository dao;

	public Flux<Actor> getAll() {
		return dao.findAll();
	}

	public Flux<Actor> getAll(Pageable page) {
		return dao.findAll().skip(page.getPageNumber() * page.getPageSize()).take(page.getPageSize());
	}

	public Mono<Actor> getOne(int id) {
		return dao.findById(id).single();

	}

	public Mono<Actor> add(Actor item) {
		if (item == null)
			return Mono.error(new InvalidDataException("No puede ser nulo"));
		if (item.isInvalid())
			return Mono.error(new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields()));

		if (item.getActorId() == 0)
			return dao.save(item)
					.onErrorMap(original -> new InvalidDataException("Mi error: " + original.getMessage(), original));
		else
			return dao.existsById(item.getActorId())
					.flatMap(exists -> exists ? Mono.error(new DuplicateKeyException("Ya existe")) : dao.save(item))
					.onErrorMap(original -> new InvalidDataException("Mi error: " + original.getMessage(), original));
	}

	public Mono<Actor> modify(Actor item) {
		if (item == null)
			return Mono.error(new InvalidDataException("No puede ser nulo"));
		if (item.isInvalid())
			return Mono.error(new InvalidDataException(item.getErrorsMessage(), item.getErrorsFields()));
		return dao.save(item)
				.onErrorMap(original -> new NotFoundException());

//		return dao.existsById(item.getActorId())
//				.flatMap(exists -> exists ? dao.save(item) : Mono.error(new NotFoundException()));
	}

	public Mono<Void> delete(Actor item) {
		if (item == null)
			return Mono.error(new InvalidDataException("No puede ser nulo"));
		return dao.delete(item).switchIfEmpty(Mono.error(new NotFoundException()));
	}

	public Mono<Void> deleteById(int id) {
		return dao.deleteById(id).switchIfEmpty(Mono.error(new NotFoundException()));
	}

}
