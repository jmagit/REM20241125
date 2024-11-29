package com.example.ioc.basico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.ioc.NotificationService;

@Repository
//@Profile("default")
public class RepositorioImpl implements Repositorio {
	private final Configuracion configuracion;
	@Autowired 
	private ApplicationEventPublisher publisher;
	protected void doEvent(@NonNull String event) { 
		publisher.publishEvent(event); 
	}	
	
	public RepositorioImpl(Configuracion configuracion, NotificationService notify) {
		notify.add(getClass().getSimpleName() + " Constructor");
		this.configuracion = configuracion;
		// configuracion.config();
	}


	@Override
	public void save() {
		var contador = configuracion.getNext();
		System.err.println("Guardo los datos. Me han usado " + contador + (contador == 1 ? " vez." : " veces."));
		doEvent("Han ejecutado el guardar.");
	}
}