package com.example.ioc.basico;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("remoto")
public class CualificaRemotoImpl implements Cualifica {
	@Override
	public void guarda() {
		System.out.println("Envio a remoto");
	}
}
