package com.example.telemetry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.EventListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Profile("tele")
public class EventListenerImpl implements EventListener {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private record Status(TelemetryEvent prior, TelemetryEvent current) {
		public String getStatus() {
			if(prior == null || "down".equalsIgnoreCase(prior.estado())) return "starting";
			return "up".equalsIgnoreCase(current.estado()) ? "running" : "stopped";
		}
		public String getHealth() {
			if(prior == null || "down".equalsIgnoreCase(prior.estado())) return "gray";
			var diff = (current.enviado().getTime() - prior.enviado().getTime()) / 1000;
			return diff > 3 ? "red" : (diff > 2 ? "yellow" : "green");
		}
	}
	
	Map<String, Status> store = new HashMap<>();

	ObjectMapper mapper = new ObjectMapper();

	@KafkaListener(topics = "${app.topic.name}-control", topicPattern = "${app.topic.name}-logger")
	public void listenWithHeaders(@Header(KafkaHeaders.RECEIVED_KEY) String key, @Payload String value,
			@Header(KafkaHeaders.OFFSET) String offset) {
		TelemetryEvent event;
		try {
			event = mapper.readValue(value, TelemetryEvent.class);
			store.put(key, new Status(store.containsKey(key) ? store.get(key).current : null, event));
			System.out.println("----- Estado Actual ------------------");
			store.forEach((clave, valor) -> {
				if("red".equals(valor.getHealth())) {
					System.err.println(String.format("%s, status: %s, health: %s, prior: %s, current: %s", 
							clave, valor.getStatus(), valor.getHealth(), valor.prior.enviado().toString(), valor.current.enviado().toString()));
				} else {
					System.out.println(String.format("%s, status: %s, health: %s", clave, valor.getStatus(), valor.getHealth()));
				}
			});
		} catch (JsonProcessingException ex) {
			System.err.println(ex.getMessage());
			logger.error(String.format("KEY: %s OFFSET: %s Invalid format in %s",  offset, key, value));
		}
	}
}
