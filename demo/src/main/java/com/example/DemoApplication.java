package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.err.println("Aplicacion arrancada");
	}

	@Value("${mi.valor:Valor por defecto}")
	private String config;
	
	@Bean
	@Order(1)
	CommandLineRunner ioc() {
		return arg -> {
			System.err.println(config);
//			Servicio srv = new ServicioImpl(new RepositorioImpl(new ConfiguracionImpl()));
//			var contexto = new AnnotationConfigApplicationContext(AppConfig.class);
////			Servicio srv = contexto.getBean(Servicio.class);
////			srv.add();
////			contexto.getBean(ClaseNoComponente.class).saluda();
////			contexto.getBean(NotificationService.class).getListado().forEach(System.out::println);
////			contexto.getBean(Sender.class).send("un mensaje");
//			contexto.close();
		};
	}
}
