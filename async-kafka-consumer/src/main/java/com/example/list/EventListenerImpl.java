package com.example.list;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.EventListener;

@Service
@Profile("list")
public class EventListenerImpl implements EventListener {
	@KafkaListener(topics = "${app.topic.name}", topicPattern = "${app.topic.name}")
	public void listenWithHeaders(@Header(KafkaHeaders.RECEIVED_KEY) String key, @Payload String value,
			@Header(KafkaHeaders.OFFSET) String offset) {
		System.out.println(String.format("KEY: %s, MESSAGE: %s, OFFSET: %s", key, value, offset));
	}
}
