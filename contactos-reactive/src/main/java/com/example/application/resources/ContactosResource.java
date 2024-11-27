package com.example.application.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
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

import com.example.domains.contracts.repositories.ContactosRepository;
import com.example.domains.entities.Contacto;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = {"/contactos/v1"})
public class ContactosResource {
	@Autowired
	private ContactosRepository dao;

	interface ContactoShort {
		@Value("#{target._id}")
		String getId();
		String getTratamiento();
		String getNombre();
		String getApellidos();
		String getTelefono();
		String getEmail();
		String getSexo();	
	}

	@Hidden
	@GetMapping
	public Flux<ContactoShort> getAll(@RequestParam(required = false, name="search") String fragmento) {
		return fragmento == null ? dao.findAllBy(ContactoShort.class) : dao.searchTop20ByNombreContainsOrderByNombreAsc(fragmento, ContactoShort.class);
	}

	@GetMapping(params = "page")
	@Operation(summary = "Listar todos")
	public Flux<Contacto> getAll(
			@Parameter(description = "Buscar que contenga en el nombre", required = false) 
			@RequestParam(required = false, name="search") 
			String fragmento, 
			@Parameter(description = "Zero-based page index (0..N)", required = false) 
			@RequestParam(required = true, defaultValue = "0") int page, 
			@Parameter(description = "The size of the page to be returned", required = false) 
			@RequestParam(required = true, defaultValue = "20") int rows) {
		// sort	array[string] Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.
		return fragmento == null ? dao.findAll().skip(page*rows).take(rows) : 
			dao.searchByNombreContainsOrderByNombreAsc(fragmento, PageRequest.of(page, rows), Contacto.class);
	}

	@GetMapping(path = "/confictivos")
	@Operation(summary = "Listar confictivos")
	public Flux<Contacto> getConfictivos() {
		return dao.findByConflictivoTrue();
	}

	@GetMapping(path = "/{id}")
	@Operation(summary = "Consultar un contacto")
//	@Secured({ "ROLE_ADMINISTRADORES" })
	@SecurityRequirement(name = "bearerAuth")
	public Mono<Contacto> getOne(@PathVariable String id) throws Exception {
		return dao.findById(id).single().onErrorMap(original -> new NotFoundException(original));
	}

//	@PreAuthorize("isAuthenticated()")
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	@Operation(summary = "Creación de un contacto")
	@SecurityRequirement(name = "bearerAuth")
	public Mono<ResponseEntity<Object>> add(@Valid @RequestBody Contacto item, ServerHttpRequest req) throws Exception {
		return dao.insert(item)
				.map(data -> ResponseEntity.created(URI.create(req.getURI().toString() + "/" + data.getId())).build())
				.onErrorMap(original -> original instanceof org.springframework.dao.DuplicateKeyException ? 
						new DuplicateKeyException("Ya existe") : new InvalidDataException(original.getMessage(), original));
	}

	@PutMapping(path = "/{id}")
	@Operation(summary = "Modificación de un contacto")
	@SecurityRequirement(name = "bearerAuth")
	public Mono<Contacto> modify(@PathVariable String id, @Valid @RequestBody Contacto item) throws Exception {
		if(!id.equals(item.getId()))
			return Mono.error(new BadRequestException("No coinciden los ID"));
		return dao.findById(id).flatMap(data -> dao.save(item)).switchIfEmpty(Mono.error(new NotFoundException()));
	}

	@DeleteMapping(path = "/{id}")
//	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@Operation(summary = "Eliminación de un contacto")
	@SecurityRequirement(name = "bearerAuth")
	public Mono<ResponseEntity<Void>> delete(@PathVariable String id) throws Exception {
//		return dao.deleteById(id).switchIfEmpty(Mono.error(new NotFoundException()));
		return dao.findById(id)
				.flatMap(data -> dao.delete(data)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
				.switchIfEmpty(Mono.error(new NotFoundException()));
	}

}
