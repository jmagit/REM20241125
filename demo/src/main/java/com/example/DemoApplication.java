package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import com.example.domains.contracts.repositories.ActoresRepository;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.example.application.proxies")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@Primary
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	@LoadBalanced
	RestTemplate restTemplateLB(RestTemplateBuilder builder) {
		return builder.build();
	}

	@LoadBalanced
	@Bean
	RestClient.Builder restClientBuilderLB() {
		return RestClient.builder();
	}

	@Primary
	@Bean
	RestClient.Builder restClientBuilder() {
		return RestClient.builder();
	}

	@Override
//	@Transactional
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada");
//		var list = dao.findByActorIdGreaterThan(195);
//		list.forEach(a -> {
//			System.out.println(a + " peliculas: " + a.getFilmActors().size());
//			a.getFilmActors().forEach(p -> System.out.println("\t" + p.getFilm().getTitle()));
//		});		
	}

	@Autowired
	ActoresRepository dao;

	@Bean
	CommandLineRunner datos(ActoresRepository dao) {
		return arg -> {
//			var nuevo = dao.save(new Actor(0, "Pepito", "Grillo"));
//			var item = dao.findById(nuevo.getActorId());
//			if(item.isPresent()) {
//				var leido = item.get();
//				System.out.println(leido);
//				leido.setFirstName(leido.getFirstName().toUpperCase());
//				dao.save(leido);
//			} else {
//				System.err.println("No encontrado " + nuevo.getActorId());
//			}
//			
//			dao.findAll().forEach(System.out::println);
//			dao.deleteById(nuevo.getActorId());
//			dao.findTop5ByFirstNameStartingWithIgnoreCaseOrderByLastNameDesc("p").forEach(System.out::println);
//			dao.findTop5ByFirstNameStartingWithIgnoreCase("p", Sort.by("FirstName").descending()).forEach(System.out::println);
//			dao.findByActorIdGreaterThan(195).forEach(System.out::println);
//			dao.findByJPQL(195).forEach(System.out::println);
//			dao.findBySQL(195).forEach(System.out::println);
//			dao.findAll((root, query, builder) -> builder.greaterThan(root.get("actorId"), 195)).forEach(System.out::println);
//			dao.findAll((root, query, builder) -> builder.lessThanOrEqualTo(root.get("actorId"), 5)).forEach(System.out::println);
//			var nuevo = dao.insert(new Actor(1, "Pepito", "Grillo"));
//			var nuevo = new Actor(0, null, "AA"); // new Actor(222, "Pepito", "Grillo");
//			if(nuevo.isValid()) {
//				dao.insert(nuevo);
//			} else {
//				System.err.println(nuevo.getErrorsMessage());
//			}
//			var item = dao.findById(1);
//			if(item.isPresent()) {
//				var leido = item.get();
//				System.out.println(leido);
//				leido.getFilmActors().forEach(p -> System.out.println("\t" + p.getFilm().getTitle()));
//			} else {
//				System.err.println("No encontrado ");
//			}
//			dao.findByActorIdGreaterThan(195).forEach(item -> System.out.println(ActorDTO.from(item)));
//			dao.queryByActorIdGreaterThan(195).forEach(System.out::println);
//			dao.readByActorIdGreaterThan(195).forEach(item -> System.out.println(item.getActorId() + " " + item.getNombre()));
//			dao.searchByActorIdGreaterThan(195, ActorDTO.class).forEach(System.out::println);
//			dao.searchByActorIdGreaterThan(195, ActorShort.class).forEach(item -> System.out.println(item.getActorId() + " " + item.getNombre()));
//			dao.findAllBy(ActorDTO.class).forEach(System.out::println);
//			var json = new ObjectMapper();
//			dao.searchByActorIdGreaterThan(195, ActorDTO.class).forEach(item -> {
//				try {
//					System.out.println(json.writeValueAsString(item));
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//			var xml = new com.fasterxml.jackson.dataformat.xml.XmlMapper();
//			dao.searchByActorIdGreaterThan(195, ActorDTO.class).forEach(item -> {
//				try {
//					System.out.println(xml.writeValueAsString(item));
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
//			dao.findByActorIdGreaterThan(195).forEach(item -> {
//				try {
//					System.out.println(json.writeValueAsString(item));
//				} catch (JsonProcessingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			});
		};
	}

//	@Value("${mi.valor:Valor por defecto}")
//	private String config;
//	
//	@Bean
//	CommandLineRunner ioc() {
//		return arg -> {
//			System.err.println(config);
////			Servicio srv = new ServicioImpl(new RepositorioImpl(new ConfiguracionImpl()));
////			var contexto = new AnnotationConfigApplicationContext(AppConfig.class);
//////			Servicio srv = contexto.getBean(Servicio.class);
//////			srv.add();
//////			contexto.getBean(ClaseNoComponente.class).saluda();
//////			contexto.getBean(NotificationService.class).getListado().forEach(System.out::println);
//////			contexto.getBean(Sender.class).send("un mensaje");
////			contexto.close();
//		};
//	}

}
