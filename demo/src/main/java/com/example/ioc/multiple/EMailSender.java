package com.example.ioc.multiple;

import org.springframework.stereotype.Component;

@Component
public class EMailSender implements Sender {

	@Override
	public void send(String message) {
		System.err.println("Envio correo: " + message);
	}

}
