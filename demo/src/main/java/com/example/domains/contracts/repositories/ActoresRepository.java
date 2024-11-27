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

import com.example.domains.entities.Actor;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.NotFoundException;

public interface ActoresRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {
	List<Actor> findTop5ByFirstNameStartingWithIgnoreCaseOrderByLastNameDesc(String prefijo);
	List<Actor> findTop5ByFirstNameStartingWithIgnoreCase(String prefijo, Sort orderBy);
	
//	@EntityGraph(attributePaths = {"filmActors", "filmActors.film"})
	@Query("from Actor a LEFT JOIN FETCH a.filmActors p JOIN FETCH p.film WHERE a.actorId > ?1")
	List<Actor> findByActorIdGreaterThan(int actorId);
	@Query(value = "from Actor a where a.actorId > ?1")
	List<Actor> findByJPQL(int actorId);
	@Query(value = "SELECT * FROM actor WHERE actor_id > :id", nativeQuery = true)
	List<Actor> findBySQL(@Param("id") int actorId);
	
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
