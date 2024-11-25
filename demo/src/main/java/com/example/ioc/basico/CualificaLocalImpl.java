package com.example.ioc.basico;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("local")
public class CualificaLocalImpl implements Cualifica {
	@Override
	public void guarda() {
		System.out.println("Guardo en local");
	}
}
