package com.example.ioc.basico;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.ioc.NotificationService;

import jakarta.annotation.PostConstruct;

public class ClaseNoComponente {
	@Autowired
	private NotificationService notify;

	private final Configuracion configuracion;
	
	public ClaseNoComponente(Configuracion configuracion) {
		this.configuracion = configuracion;
	}
	@PostConstruct
	private void despuesDelConstructor() {
		notify.add(getClass().getSimpleName() + " Constructor");
	}
	
	public void saluda() {
		System.out.println("Hola mundo");
		notify.add("Hola mundo");
	}
}
