package com.example.logs;

import java.util.Date;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.EventListener;

@Service
@Profile("logs")
public class EventListenerImpl implements EventListener {
	@KafkaListener(topics = "${app.topic.name}-logger", topicPattern = "${app.topic.name}-logger")
	public void listenWithHeaders(@Header(KafkaHeaders.RECEIVED_KEY) String key, @Payload String value,
			@Header(KafkaHeaders.ORIGINAL_TIMESTAMP) ConsumerRecord metadata) {
//		System.out.println(String.format("%s %s %s", (new Date(metadata.timestamp())).toString(), key, value));
		System.out.println(value);
	}
}
