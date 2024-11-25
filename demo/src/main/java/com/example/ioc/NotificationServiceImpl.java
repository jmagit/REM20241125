package com.example.ioc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("notificaciones")
//@Scope("prototype")
public class NotificationServiceImpl implements NotificationService {
	private List<String> listado = new ArrayList<>();
	
	@Override
	public List<String> getListado() {
		return listado.stream().toList();
	}
	
	@Override
	public Optional<String> getMessage(int index) {
		if( 0 > index || index >= listado.size())
			return Optional.empty();
		return Optional.of(listado.get(index));
	}

	@Override
	public void add(String message) {
		if(message == null || message.trim() == "")
			throw new IllegalArgumentException("Falta el mensaje.");
		listado.add(message);
	}
	@Override
	public void delete(int index) {
		if( 0 > index || index >= listado.size())
			throw new IndexOutOfBoundsException();
		listado.remove(index);
	}
	@Override
	public void clear() {
		listado.clear();
	}
	
}
