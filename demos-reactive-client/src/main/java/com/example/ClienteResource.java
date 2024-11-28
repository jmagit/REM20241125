package com.example;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class ClienteResource {
	@Value("${app.servidor.url}")
	private String urlServidor;
	
	@GetMapping("/page/rapido")
	Flux<Persona> rapido(@RequestParam(required = true, defaultValue = "20") int rows) {
		WebClient client = WebClient.create(urlServidor);
		return Flux.merge(
				client.get().uri("/actores/v1?page=0&rows=" + rows).retrieve().bodyToFlux(Persona.class), 
				client.get().uri("/actores/v1?page=1&rows=" + rows).retrieve().bodyToFlux(Persona.class), 
				client.get().uri("/actores/v1?page=2&rows=" + rows).retrieve().bodyToFlux(Persona.class)
			);
	}
	@GetMapping("/page/lento")
	Flux<Persona> lento(@RequestParam(required = true, defaultValue = "20") int rows) {
		RestTemplate rest = new RestTemplate();
		List<Persona> l1 = rest.getForObject(urlServidor + "/actores/v1?page=0&rows=" + rows, List.class);
		List<Persona> l2 = rest.getForObject(urlServidor + "/actores/v1?page=1&rows=" + rows, List.class);
		List<Persona> l3 = rest.getForObject(urlServidor + "/actores/v1?page=2&rows=" + rows, List.class);
		return Flux.merge(Flux.fromIterable(l1), Flux.fromIterable(l2),Flux.fromIterable(l3));
	}
	
	@GetMapping("/one/masrapido")
	Flux<Persona> masrapido() {
		WebClient client = WebClient.create(urlServidor);
		return Flux.merge(
				client.get().uri("/demos/db/lento/1").retrieve().bodyToMono(Persona.class), 
				client.get().uri("/demos/db/lento/2").retrieve().bodyToMono(Persona.class), 
				client.get().uri("/demos/db/lento/3").retrieve().bodyToMono(Persona.class) 
			);
	}
	@GetMapping("/one/maslento")
	Flux<Persona> maslento() {
		RestTemplate rest = new RestTemplate();
		Persona l1 = rest.getForObject(urlServidor + "/demos/db/lento/1", Persona.class);
		Persona l2 = rest.getForObject(urlServidor + "/demos/db/lento/2", Persona.class);
		Persona l3 = rest.getForObject(urlServidor + "/demos/db/lento/3", Persona.class);
		return Flux.just(l1, l2, l3);
	}
	@GetMapping("/cotilla")
	Mono<String> cotilla() {
		return WebClient.create(urlServidor).get().uri("/demos/datos/cabeceras").retrieve().bodyToMono(String.class);
	}
}
