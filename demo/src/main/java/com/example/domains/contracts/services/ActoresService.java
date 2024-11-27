package com.example.domains.contracts.services;

import java.util.List;

import com.example.domains.core.contracts.services.PagingAndSortingDomainService;
import com.example.domains.entities.Actor;

public interface ActoresService extends PagingAndSortingDomainService<Actor, Integer> {
	void repartirPremios(List<String> premios);
}
