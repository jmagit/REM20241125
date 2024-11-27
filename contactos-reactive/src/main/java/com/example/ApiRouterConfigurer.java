package com.example;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.arrayschema.Builder.arraySchemaBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;
import static org.springdoc.core.fn.builders.schema.Builder.schemaBuilder;

import java.util.function.Consumer;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springdoc.core.fn.builders.operation.Builder;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.example.application.resources.ContactosHandler;
import com.example.domains.contracts.repositories.ContactosRepository;
import com.example.domains.entities.Contacto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Configuration
@EnableWebFlux
public class ApiRouterConfigurer implements WebFluxConfigurer {

	@Bean
	@RouterOperations({
       @RouterOperation(method = RequestMethod.GET, path = "/contactos/v2", operation = @Operation( 
       		operationId = "getAll", tags = "contacto-functions", 
       		parameters = { @Parameter(in = ParameterIn.QUERY, name = "fragmento", required = false, schema = @Schema(type = "string"))}
       )),
       @RouterOperation(method = RequestMethod.GET, path = "/contactos/v2/{id}", operation = @Operation( 
       		operationId = "getOne", tags = "contacto-functions", 
       		parameters = { @Parameter(in = ParameterIn.PATH, name = "id", required = true, schema = @Schema(type = "string")) }
       )),
       @RouterOperation(method = RequestMethod.POST, path = "/contactos/v2", operation = @Operation( 
       		operationId = "create", tags = "contacto-functions", 
       		requestBody = @RequestBody( required = true, content = @Content( schema = @Schema(type = "object")))
       )),
       @RouterOperation(method = RequestMethod.PUT, path = "/contactos/v2/{id}", operation = @Operation( 
       		operationId = "update", tags = "contacto-functions", 
       		parameters = { @Parameter(in = ParameterIn.PATH, name = "id", required = true, schema = @Schema(type = "string")) }, 
       		requestBody = @RequestBody( required = true, content = @Content( schema = @Schema(type = "object")))
       )),
       @RouterOperation(method = RequestMethod.DELETE, path = "/contactos/v2/{id}", operation = @Operation( 
       		operationId = "delete", tags = "contacto-functions", 
       		parameters = { @Parameter(in = ParameterIn.PATH, name = "id", required = true, schema = @Schema(type = "string")) }
       ))
	})
	public RouterFunction<?> routerContactos(ContactosHandler handler) {
		return RouterFunctions.route().path("/contactos/v2", ops -> ops
				.GET("/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getOne)
				.GET(RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getAll)
				.POST("", RequestPredicates.contentType(MediaType.APPLICATION_JSON), handler::create)
				.PUT("/{id}", RequestPredicates.contentType(MediaType.APPLICATION_JSON), handler::update)
				.DELETE("/{id}", handler::delete)
				).build();
//		var ruta = RouterFunctions.route()
//				.GET("/contactos/v2", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getAll)
//				.GET("/contactos/v2/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getOne);
//		ruta = ruta
//				.POST("/contactos/v2", RequestPredicates.contentType(MediaType.APPLICATION_JSON), handler::create)
//				.PUT("/contactos/v2/{id}", RequestPredicates.contentType(MediaType.APPLICATION_JSON), handler::update)
//				.DELETE("/contactos/v2/{id}", handler::delete);
//		return ruta.build();
	}

	@Bean
	public RouterFunction<?> springdocRouterContactos(ContactosHandler handler) {
		var path = "/contactos/v3";
		return SpringdocRouteBuilder.route()
				.GET(path, RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getAll, getOp(handler.getClass(), "getAll"))
				.POST(path, RequestPredicates.contentType(MediaType.APPLICATION_JSON), handler::create, getOp(handler.getClass(), "create"))
				.GET(path + "/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), handler::getOne, getOp(handler.getClass(), "getOne"))
				.PUT(path + "/{id}", RequestPredicates.contentType(MediaType.APPLICATION_JSON), handler::update, getOp(handler.getClass(), "update"))
				.DELETE(path + "/{id}", handler::delete, getOp(handler.getClass(), "delete"))
				.build();
	}

	private Consumer<Builder> getOp(Class<?> handler, String method) {
		return ops -> ops.beanClass(handler).beanMethod(method);
	}

	@Bean
	RouterFunction<?> routerGeneradoContactos(ContactosRepository dao) {
		return generateRouterFunction("/contactos/v4", dao, Contacto.class);
	}
	
	private <E> RouterFunction<?> generateRouterFunction(String path, ReactiveMongoRepository<E, String> dao, Class<E> entidad) {
		return  SpringdocRouteBuilder.route()
				.GET(path, RequestPredicates.accept(MediaType.APPLICATION_JSON), 
						request -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(dao.findAll(), entidad)
						, getOpenAPI(entidad, "getAll")
				).GET(path + "/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), 
						request -> dao.findById(request.pathVariable("id"))
							.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(item))
							.switchIfEmpty(ServerResponse.notFound().build())
							, getOpenAPI(entidad, "getOne")
				).POST(path, RequestPredicates.contentType(MediaType.APPLICATION_JSON), 
						request -> request.bodyToMono(entidad)
							.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(dao.save(item), entidad))			
							, getOpenAPI(entidad, "add")
				).PUT(path + "/{id}", RequestPredicates.contentType(MediaType.APPLICATION_JSON), 
						request -> request.bodyToMono(entidad)
							.flatMap(item -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(dao.save(item), entidad))
							, getOpenAPI(entidad, "modify")
				).DELETE(path + "/{id}", 
						request -> dao.deleteById(request.pathVariable("id")).then(ServerResponse.noContent().build())
						, getOpenAPI(entidad, "delete")
				).build();
	}
	
	private Consumer<Builder> getOpenAPI(Class<?> entidad, String method) {
		return ops -> {
			ops.tag(entidad.getSimpleName().toLowerCase() + "-generated")
				.operationId(method);
			var paramId = parameterBuilder().in(ParameterIn.PATH).name("id").required(true);
			var schema = schemaBuilder().implementation(entidad);
			var content = contentBuilder().schema(schema);
			var requestBody = requestBodyBuilder().content(content);
			switch (method) {
			case "getAll":
				ops.response(responseBuilder().responseCode("200").content(contentBuilder().array(arraySchemaBuilder().schema(schema))));
				break;
			case "getOne":
				ops.parameter(paramId);
				ops.response(responseBuilder().responseCode("200").content(content));
				ops.response(responseBuilder().responseCode("404").implementation(ProblemDetail.class));
				break;
			case "add":
				ops.requestBody(requestBody);
				ops.response(responseBuilder().responseCode("201"));
				ops.response(responseBuilder().responseCode("400").implementation(ProblemDetail.class));
				break;
			case "modify":
				ops.requestBody(requestBody);
				ops.response(responseBuilder().responseCode("400"));
				ops.response(responseBuilder().responseCode("404").implementation(ProblemDetail.class));
			case "delete":
				ops.parameter(paramId);
				ops.response(responseBuilder().responseCode("204"));
				ops.response(responseBuilder().responseCode("404").implementation(ProblemDetail.class));
				break;
			default:
				break;
			}
		};
	}

}
