package com.example.domains.contracts.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domains.entities.Category;

public interface CategoriasRepository extends JpaRepository<Category, Integer> {

}
