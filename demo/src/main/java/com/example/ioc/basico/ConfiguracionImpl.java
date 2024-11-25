package com.example.ioc.basico;

import org.springframework.stereotype.Component;

import com.example.ioc.NotificationService;

@Component
public class ConfiguracionImpl implements Configuracion {
	private int contador = 0;
	
	public ConfiguracionImpl(NotificationService notify) {
		notify.add(getClass().getSimpleName() + " Constructor");
	}

	@Override
	public void config() {
		System.err.println("Me configuran");	
	}

	@Override
	public int getNext() {
		return ++contador;
	}

}
