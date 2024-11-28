package com.example.domains.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;


/**
 * The persistent class for the actor database table.
 * 
 */
@Table(name="actor")
public class Actor extends EntityBase<Actor> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column("actor_id")
	private int actorId;

	@Column("first_name")
	@NotBlank
	@Size(max=45, min=2)
	private String firstName;

	@Column("last_name")
	@Size(max=45, min=2)
//	@Pattern(regexp = "[A-Z]+", message = "Tiene que estar en mayusculas")
	private String lastName;

	@Column("last_update") //, insertable=false, updatable=false, nullable=false)
	@PastOrPresent
	@Transient
	private Timestamp lastUpdate;

	public Actor() {
	}
	
	public Actor(int actorId) {
		super();
		this.actorId = actorId;
	}

	public Actor(int actorId, String firstName, String lastName) {
		super();
		this.actorId = actorId;
		this.firstName = firstName;
		this.lastName = lastName;
	}


	public int getActorId() {
		return this.actorId;
	}

	public void setActorId(int actorId) {
		this.actorId = actorId;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Timestamp getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actorId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actor other = (Actor) obj;
		return actorId == other.actorId;
	}

	@Override
	public String toString() {
		return "Actor [actorId=" + actorId + ", firstName=" + firstName + ", lastName=" + lastName + ", lastUpdate="
				+ lastUpdate + "]";
	}

	public void jubilate() {
		
	}
	
	public void recibePremio(String premio) {
		
	}
}