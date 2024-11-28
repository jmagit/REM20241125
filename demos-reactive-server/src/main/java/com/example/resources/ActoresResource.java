package com.example.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.domains.entities.Actor;
import com.example.domains.services.ActoresService;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;
import com.example.repositories.ActoresRepository;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/actores/v1")
public class ActoresResource {
	@Autowired
	ActoresRepository dao;

	@Autowired
	ActoresService srv;

	@GetMapping
	public Flux<Actor> getAll(@RequestParam(required = true, defaultValue = "0") int page, @RequestParam(required = true, defaultValue = "20") int rows) {
//		return dao.findAll().skip(page*rows).take(rows);
		return srv.getAll(PageRequest.of(page, rows));
	}
	@GetMapping("/search")
	public Flux<Actor> getSearch(@RequestParam(required = false, defaultValue = "") String nombre, @RequestParam(required = false, defaultValue = "") String apellidos) {
		if(nombre.isBlank())
			return dao.searchByLastNameContainsIgnoreCase(apellidos);
		if(apellidos.isBlank())
			return dao.searchByFirstNameContainsIgnoreCase(nombre);
		return dao.searchByFirstNameContainsAndLastNameContainsAllIgnoreCase(nombre, apellidos);		
	}
	
	
	@GetMapping(path = "/{id}")
	public Mono<Actor> getOne(@PathVariable int id) {
//		return dao.findById(id).single().onErrorMap(original -> new NotFoundException(original));
		return srv.getOne(id).onErrorMap(original -> new NotFoundException(original));
	}
	@PostMapping
	public Mono<Actor> create(@RequestBody Actor item) {
		return srv.add(item);
//		return dao.save(item).onErrorMap(original -> new InvalidDataException("Mi error: " + original.getMessage(), original));
	}
	@PutMapping(path = "/{id}")
	public Mono<Actor> update(@PathVariable int id, @Valid @RequestBody Actor item) {
		if(id != item.getActorId())
			return Mono.error(new InvalidDataException("Mal las claves"));
		return srv.modify(item); 
	}
	
	@DeleteMapping(path = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public Mono<Void> delete(@PathVariable int id) {
		return srv.deleteById(id).switchIfEmpty(Mono.error(new NotFoundException()));
	}

}
