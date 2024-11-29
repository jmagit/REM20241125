package com.example.domains.contracts.repositories;

import java.util.List;
import java.util.Optional;

import org.hibernate.sql.Insert;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.example.domains.core.contracts.repositories.ProjectionsAndSpecificationJpaRepository;
import com.example.domains.core.contracts.repositories.RepositoryWithProjections;
import com.example.domains.entities.Actor;
import com.example.domains.entities.models.ActorDTO;
import com.example.domains.entities.models.ActorShort;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.NotFoundException;

@RepositoryRestResource(exported = false)
public interface ActoresRepository extends ProjectionsAndSpecificationJpaRepository<Actor, Integer> {
	// JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor>, RepositoryWithProjections {
	List<Actor> findTop5ByFirstNameStartingWithIgnoreCaseOrderByLastNameDesc(String prefijo);
	List<Actor> findTop5ByFirstNameStartingWithIgnoreCase(String prefijo, Sort orderBy);
	
//	@EntityGraph(attributePaths = {"filmActors", "filmActors.film"})
	@Query("from Actor a LEFT JOIN FETCH a.filmActors p JOIN FETCH p.film WHERE a.actorId > ?1")
	List<Actor> findByActorIdGreaterThan(int actorId);
	@Query(value = "from Actor a where a.actorId > ?1")
	List<Actor> findByJPQL(int actorId);
	@Query(value = "SELECT * FROM actor WHERE actor_id > :id", nativeQuery = true)
	List<Actor> findBySQL(@Param("id") int actorId);

	List<ActorDTO> queryByActorIdGreaterThan(int actorId);
	List<ActorShort> readByActorIdGreaterThan(int actorId);
	@RestResource(exported = false)
	<T> List<T> searchByActorIdGreaterThan(int actorId, Class<T> proyeccion);
	
	@Override
	@EntityGraph(attributePaths = {"filmActors", "filmActors.film"})
	Optional<Actor> findById(Integer id);
	
	default Actor insert(Actor item) throws DuplicateKeyException {
		if(item.getActorId() != 0 && existsById(item.getActorId()))
			throw new DuplicateKeyException();
		return save(item);
	}
	default Actor update(Actor item) throws NotFoundException {
		if(!existsById(item.getActorId()))
			throw new NotFoundException();
		return save(item);
	}
	
}
