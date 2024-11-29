package com.example.calculate;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.EventListener;

@Service
@Profile("calc")
public class EventListenerImpl implements EventListener {
	Map<String, Integer> contadores = new HashMap<>();
	
	@KafkaListener(topics = "${app.topic.name}", topicPattern = "${app.topic.name}")
	public void listenWithHeaders(@Header(KafkaHeaders.RECEIVED_KEY) String key, @Payload String value,
			@Header(KafkaHeaders.OFFSET) String offset) {
		int count = contadores.containsKey(key) ? contadores.get(key) + 1 : 1;
		contadores.put(key, count);
		System.out.println("Nuevo Resumen");
		contadores.forEach((clave, valor) -> {
			System.out.println(String.format("KEY: %s, COUNT: %s", clave, valor));
		});
	}
}
