package com.example.domains.entities.models;

import org.springframework.beans.factory.annotation.Value;

public interface ActorShort {
	int getActorId();
	
	@Value("#{target.lastName + ', ' + target.firstName}")
	String getNombre();
}
