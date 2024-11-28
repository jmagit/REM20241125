package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class DemosReactiveClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemosReactiveClientApplication.class, args);
	}

	@Value("${app.servidor.url}")
	private String urlServidor;
	
	@Bean
	CommandLineRunner suscribe() {
		return arg -> {
			var flux = WebClient.create(urlServidor)
					.get().uri("/actores/v1?page=0&rows=10")
					.accept(MediaType.APPLICATION_NDJSON)
					.retrieve().bodyToFlux(Persona.class);
			var flux2 = WebClient.create(urlServidor)
					.get().uri("/actores/v1?page=2&rows=10")
					.accept(MediaType.APPLICATION_NDJSON)
					.retrieve().bodyToFlux(Persona.class);
			flux.subscribe(item -> System.out.println("recibido1->" + item), err -> err.printStackTrace());
			flux2.subscribe(item -> System.out.println("recibido2->" + item), err -> err.printStackTrace());
		};
	}
}
