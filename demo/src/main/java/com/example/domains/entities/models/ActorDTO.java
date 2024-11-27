package com.example.domains.entities.models;

import com.example.domains.entities.Actor;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data //@AllArgsConstructor
@JacksonXmlRootElement(localName = "Actor")
public class ActorDTO {
	private int id;
	@JsonProperty("nombre")
	private String firstName;
	private String lastName;

	public ActorDTO(int actorId, String firstName, String lastName) {
		super();
		this.id = actorId;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public static ActorDTO from(Actor source) {
		return new ActorDTO(
				source.getActorId(), 
				source.getFirstName(), 
				source.getLastName()
				);		
	}

	public static Actor from(ActorDTO source) {
		return new Actor(
				source.getId(), 
				source.getFirstName(), 
				source.getLastName()
				);		
	}
}
