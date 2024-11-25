package com.example.ioc.basico;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class ServicioMockImpl implements Servicio {
	
	public ServicioMockImpl() {
//		System.err.println(getClass().getSimpleName() + " Constructor");
	}


	@Override
	public void add() {
		System.err.println("doble de pruebas");	
	}


	@Override
	public void get() {
		// TODO Auto-generated method stub
		
	}

}
