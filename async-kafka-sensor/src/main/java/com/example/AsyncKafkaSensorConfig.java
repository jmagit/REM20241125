package com.example;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class AsyncKafkaSensorConfig {
//  @Bean
//	public KafkaTemplate<?, ?> kafkaTemplate(ProducerFactory<Object, Object> kafkaProducerFactory,
//			ProducerListener<Object, Object> kafkaProducerListener,
//			ObjectProvider<RecordMessageConverter> messageConverter) {
//		PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
//		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
//		messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
//		map.from(kafkaProducerListener).to(kafkaTemplate::setProducerListener);
//		return kafkaTemplate;
//	}
	@Bean
	KafkaTemplate<?, ?> kafkaTemplate(@Value("${spring.kafka.bootstrap-servers}") String brokers) {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
		return kafkaTemplate;
	}

	@Bean
	KafkaTemplate<String, TelemetryEvent> telemetriaTemplate(ProducerFactory<String, TelemetryEvent> telemetriaFactory,
			NewTopic topicTelemetria) {
		var kafkaTemplate = new KafkaTemplate<>(telemetriaFactory);
		kafkaTemplate.setDefaultTopic(topicTelemetria.name());
		return kafkaTemplate;
	}

	@Bean
	ProducerFactory<String, TelemetryEvent> telemetriaFactory(
			@Value("${spring.kafka.bootstrap-servers}") String brokers) {
		return new DefaultKafkaProducerFactory<>(telemetriaSenderProps(brokers));
	}

	private Map<String, Object> telemetriaSenderProps(String brokers) {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

}
