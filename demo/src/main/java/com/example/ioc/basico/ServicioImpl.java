package com.example.ioc.basico;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.example.ioc.NotificationService;

@Service
//@Primary
//@Profile("default")
public class ServicioImpl implements Servicio {
	private final Repositorio dao;
	private final NotificationService notify;
	
	public ServicioImpl(Repositorio dao, NotificationService notify) {
		this.dao = dao;
		this.notify = notify;
		notify.add(getClass().getSimpleName() + " Constructor");
	}


	@Override
	public void add() {
		dao.save();
	}
	
	@Override
	public String toString() {
		return String.join(" -> ", notify.getListado());
	}


	@Override
	public void get() {
		add();
		
	}

}
