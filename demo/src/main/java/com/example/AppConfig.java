package com.example;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.ioc.basico.ClaseNoComponente;
import com.example.ioc.basico.Configuracion;
import com.example.ioc.basico.ConfiguracionImpl;
import com.example.ioc.multiple.EMailSender;
import com.example.ioc.multiple.FileSender;
import com.example.ioc.multiple.Sender;
import com.example.ioc.multiple.TwitterSender;

@Configuration
@ComponentScan("com.example.ioc")
public class AppConfig {
//	@Bean
//	ClaseNoComponente claseNoComponente() {
//		return new ClaseNoComponente(new ConfiguracionImpl());
//	}
	@Bean
	ClaseNoComponente claseNoComponente(Configuracion config) {
		return new ClaseNoComponente(config);
	}
	
	@Bean
	Sender sender(EMailSender item) {
		return item;
	}
	
	@Bean
	@Qualifier("remoto")
	Sender correo(EMailSender item) {
		return item;
	}
	
	@Bean
	@Qualifier("local")
	Sender fichero(FileSender item) {
		return item;
	}
	
	@Bean
	Sender twittea(TwitterSender item) {
		return item;
	}
	
	@Bean int version() { 
		return 22; 
	}
	@Bean() String autor() { 
		return "Yo mismo"; 
	}
	
	@Bean() String otroAutor(@Value("${info.app.author:(Anonimo)}") String nombre) { 
		return nombre; 
	}

}
