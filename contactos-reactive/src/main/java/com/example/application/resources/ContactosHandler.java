package com.example.application.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.domains.contracts.repositories.ContactosRepository;
import com.example.domains.entities.Contacto;
import com.example.exceptions.BadRequestException;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Service
@Tag(name =  "contactos-openapi")
public class ContactosHandler {
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

    @Operation(parameters = { @Parameter(in = ParameterIn.QUERY, name = "fragmento", required = false, schema = @Schema(type = "string"))})
	public Mono<ServerResponse> getAll(ServerRequest request) {
		var fragmento = request.queryParam("fragmento");
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(fragmento.isEmpty() ? dao.findAllBy(ContactoShort.class) : 
					dao.searchTop20ByNombreContainsOrderByNombreAsc(fragmento.get(), ContactoShort.class), ContactoShort.class);
	}

//	public HandlerFunction<ServerResponse> getOne() {
//		return request -> ok().body(
//				dao.findById(request.pathVariable("id")).switchIfEmpty(Mono.error(new NotFoundException())), Contacto.class);
//	}
	
	public Mono<ServerResponse> getOne(ServerRequest request) {
		return dao.findById(request.pathVariable("id"))
				.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(item))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> create(ServerRequest request) {
		return request.bodyToMono(Contacto.class).flatMap(item -> 
			dao.insert(item).flatMap(data -> ServerResponse.created(URI.create(request.uri().toString() + "/" + data.getId())).build()))
				.onErrorMap(original -> original instanceof org.springframework.dao.DuplicateKeyException ? 
						new DuplicateKeyException("Ya existe") : new InvalidDataException(original.getMessage(), original));
	}

	public Mono<ServerResponse> update(ServerRequest request) {
		return request.bodyToMono(Contacto.class)
				.flatMap(item -> item.getId().equals(request.pathVariable("id")) ? 
					dao.existsById(request.pathVariable("id")).flatMap(data -> data ? Mono.just(item) : Mono.error(new NotFoundException())) :
					Mono.error(new BadRequestException("No coinciden los ID")))
				.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(dao.save(item), Contacto.class));
	}
	
	public Mono<ServerResponse> delete(ServerRequest request) {
		return dao.findById(request.pathVariable("id"))
				.flatMap(data -> dao.delete(data)
						.then(ServerResponse.noContent().build()))
				.switchIfEmpty(Mono.error(new NotFoundException()));
	}

}
