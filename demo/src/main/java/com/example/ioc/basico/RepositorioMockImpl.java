package com.example.ioc.basico;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import com.example.ioc.RepositoryMock;

@RepositoryMock
public class RepositorioMockImpl implements Repositorio {
	private final Configuracion configuracion;
	
	
	public RepositorioMockImpl(Configuracion configuracion) {
//		System.err.println(getClass().getSimpleName() + " Constructor");
		this.configuracion = configuracion;
		configuracion.config();
	}


	@Override
	public void save() {
		var contador = configuracion.getNext();
		System.err.println("Guardo los datos. Me han usado " + contador + (contador == 1 ? " vez." : " veces."));	
	}
}