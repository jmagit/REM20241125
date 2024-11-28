package com.example;

import java.util.Date;
import java.util.List;
import java.util.Random;
import jakarta.annotation.PostConstruct;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.exceptions.NotFoundException;
import com.example.hash.ExternalHashCounter;
import com.example.keyvalue.ExternalCounter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(path = "/me-gusta")
public class MeGustaService {
	@Autowired
	private StringRedisTemplate template;
	@Autowired
	private RedisTemplate<String, Object> redis;

	@Autowired
	ExternalCounter counter;

	@Autowired
	ExternalHashCounter hash;

	private HashOperations<String, String, String> hashOperations;
	
	@PostConstruct
	private void inicializa() {
		hashOperations = template.opsForHash();
	}

	@GetMapping("/cont")
	@Operation(summary = "Informa de cuantos Me Gusta lleva actualmente")
	private String get() {
		return "Llevas " + counter.get();
	}

	@PostMapping("/cont")
	@Operation(summary = "Manda un Me Gusta")
	@SecurityRequirement(name = "bearerAuth")
	private String add() {
		return "Llevas " + counter.increment();
	}

	@PutMapping("/cont/{miles}")
	@Operation(summary = "Manda miles de Me Gusta")
	private String add(@Parameter(description = "Número de miles a enviar") @PathVariable int miles) {
		long actual = 0;
		Date ini = new Date();
		for (int i = 0; i++ < miles * 1000; actual = counter.increment())
			;
		return "Llevas " + actual + ". Ha tardado " + ((new Date()).getTime() - ini.getTime()) + " ms.";
	}

	@GetMapping("/hash/{id}")
	@Operation(summary = "Informa de cuantos Me Gusta lleva actualmente una pelicula")
	private String getPeli(@PathVariable int id) {
		return "Llevas " + hash.get(id) + " likes";
	}

	@PostMapping("/hash/{id}")
	@Operation(summary = "Manda un Me Gusta a una pelicula")
	@SecurityRequirement(name = "bearerAuth")
	private String addPeli(@PathVariable int id) {
		return "Llevas " + hash.increment(id);
	}

	@PutMapping("/hash/{miles}")
	@Operation(summary = "Manda miles de Me Gusta a las pelicula")
	@SecurityRequirement(name = "bearerAuth")
	private String addPeliMiles(@PathVariable int miles) {
		Random rnd = new Random();
		Date ini = new Date();
		for (int i = 0; i++ < miles * 1000; hash.increment(rnd.nextInt(1, 1001)));
		return "Ha tardado " + ((new Date()).getTime() - ini.getTime()) + " ms.";
	}
	
	@Autowired
	PeliculaContRepository dao;
	
	@GetMapping("/repo/{id}")
	@Operation(summary = "Informa de cuantos Me Gusta lleva actualmente una pelicula")
	private String getRepo(@PathVariable int id) {
		var item = dao.findById(id);
		return "Llevas " + (item.isEmpty() ? "0" : item.get().getCont()) + " likes";
	}

	@PostMapping("/repo/{id}")
	@Operation(summary = "Manda un Me Gusta a una pelicula")
	@SecurityRequirement(name = "bearerAuth")
	private String addRepo(@PathVariable int id) {
		var item = dao.findById(id);
		PeliculaCont peli;
		if(item.isEmpty()) {
			peli =new PeliculaCont(id, 1);
		} else {
			peli = item.get();
			peli.addCont();
		}
		dao.save(peli);
		return "Llevas " + peli.getCont();
	}

	@PutMapping("/repo/{miles}")
	@Operation(summary = "Manda miles de Me Gusta a las pelicula")
	@SecurityRequirement(name = "bearerAuth")
	private String addRepoMiles(@PathVariable int miles) {
		Random rnd = new Random();
		Date ini = new Date();
		for (int i = 0; i++ < miles * 1000; addRepo(rnd.nextInt(1, 101)));
		return "Ha tardado " + ((new Date()).getTime() - ini.getTime()) + " ms.";
	}

	@Autowired
	private ReactiveRedisTemplate<String, Object> react;

	@Autowired
	private ReactiveRedisTemplate<String, Pelicula> peliculaRedisTemplate;
	
//	@JsonTypeInfo(use=Id.CLASS, include = As.PROPERTY, property = "_class")
	public static record Pelicula(int id, String titulo) {}

	@GetMapping("/object/set")
	@Operation(summary = "Guarda en demo-object un objeto serializado")
	private String setJson(@RequestParam int id, @RequestParam(defaultValue = "Titulo") String titulo) {
//		peliculaRedisTemplate.opsForValue().set("demo-object", new Pelicula(id, titulo)).block();
		react.opsForValue().set("demo-object", new Pelicula(id, titulo)).block();
		return "OK";
	}
	@GetMapping("/object/get")
	@Operation(summary = "Recupera en demo-object un objeto serializado")
	private Pelicula getJson() {
//		return peliculaRedisTemplate.opsForValue().get("demo-object").map(p-> (Pelicula)p).block();
//		return react.opsForValue().get("demo-object").map(p-> (Pelicula)p).block();
		return react.opsForValue(RedisConfig.getRedisSerializationContext(Pelicula.class)).get("demo-object").map(p-> (Pelicula)p).block();
	}
	@GetMapping("/titulos/{id}")
	@Operation(summary = "Recupera el titulo de una pelicula")
	private String getTitulo(@PathVariable String id) throws NotFoundException {
		var titulo = hashOperations.get("titulos", id);
		if(titulo == null)
			throw new NotFoundException();
		return titulo;
	}

	@PostMapping("/titulos")
	@Operation(summary = "Crea un titulo de una pelicula")
	@SecurityRequirement(name = "bearerAuth")
	private String addTitulo(@RequestParam String id, @RequestParam(defaultValue = "Titulo") String titulo) {
		hashOperations.put("titulos", id, titulo);
		return titulo;
	}
	
	@Autowired
	@LoadBalanced
	RestTemplate restTemplateLB;
	
	@Data
	static class PelisDto {
	    private int id;
	    private String titulo;
	}

	@GetMapping(path = "/titulos/remoto")
	public List<PelisDto> getPelisRT() {
		ResponseEntity<List<PelisDto>> response = restTemplateLB.exchange(
				"lb://CATALOGO-SERVICE/peliculas/v1?mode=short", 
				HttpMethod.GET,
				HttpEntity.EMPTY, 
				new ParameterizedTypeReference<List<PelisDto>>() {}
		);
		var listado = response.getBody();
		listado.forEach(item -> hashOperations.put("titulos", Integer.toString(item.getId()), item.getTitulo()));
		return listado;
	}
	@GetMapping(path = "/titulos/remoto/{id}")
	public PelisDto getPelisRT(@PathVariable int id) {
			return restTemplateLB.getForObject("lb://CATALOGO-SERVICE/peliculas/v1/{key}?mode=short", PelisDto.class, id);
	}

}
