package com.example.domains.contracts.repositories;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.domains.entities.Actor;

public interface ActoresRepository extends JpaRepository<Actor, Integer>, JpaSpecificationExecutor<Actor> {
	List<Actor> findTop5ByFirstNameStartingWithIgnoreCaseOrderByLastNameDesc(String prefijo);
	List<Actor> findTop5ByFirstNameStartingWithIgnoreCase(String prefijo, Sort orderBy);
	
	List<Actor> findByActorIdGreaterThan(int actorId);
	@Query(value = "from Actor a where a.actorId > ?1")
	List<Actor> findByJPQL(int actorId);
	@Query(value = "SELECT * FROM actor WHERE actor_id > :id", nativeQuery = true)
	List<Actor> findBySQL(@Param("id") int actorId);

}
