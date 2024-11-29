package com.example;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class EventServerConfig {

	@Bean
	NewTopic topicLocation(@Value("${app.topic.name}") String name) {
		return TopicBuilder.name(name).partitions(4).replicas(1).build();
	}
	@Bean
	NewTopic topicTelemetria(@Value("${app.topic.name}-control") String name) {
		return TopicBuilder.name(name).partitions(1).replicas(1).build();
	}

	@Bean
	NewTopic topicLogger(@Value("${app.topic.name}-logger") String name) {
		return TopicBuilder.name(name).partitions(1).replicas(1).build();
	}

}
