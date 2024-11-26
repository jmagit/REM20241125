package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;

import com.example.domains.contracts.repositories.ActoresRepository;
import com.example.domains.entities.Actor;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada");
	}

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
			dao.findBySQL(195).forEach(System.out::println);
//			dao.findAll((root, query, builder) -> builder.greaterThan(root.get("actorId"), 195)).forEach(System.out::println);
			dao.findAll((root, query, builder) -> builder.lessThanOrEqualTo(root.get("actorId"), 5)).forEach(System.out::println);

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
