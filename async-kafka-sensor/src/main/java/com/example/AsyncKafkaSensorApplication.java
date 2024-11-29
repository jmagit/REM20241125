package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableScheduling
public class AsyncKafkaSensorApplication implements CommandLineRunner {
	private static final Logger LOG = Logger.getLogger(AsyncKafkaSensorApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(AsyncKafkaSensorApplication.class, args);
	}

	@Value("${app.sensor.id}")
	private String idSensor;
	@Value("${app.dorsales}")
	private int dorsales;

	@Autowired
	NewTopic topicLocation;
	@Autowired
	NewTopic topicTelemetria;
	@Autowired
	NewTopic topicLogger;

	private Random rnd = new Random();

	@Override
	public void run(String... args) throws Exception {
		sendEvent(topicLogger.name(), idSensor, String.format("%s INFO - Arranca el sensor: %s",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), idSensor));

		var peloton = new ArrayList<Integer>();
		for (var i = 1; i <= dorsales; peloton.add(i++))
			;
		Collections.shuffle(peloton);

		for (var dorsal : peloton) {
			sendEvent(topicLocation.name(), idSensor, dorsal.toString());
			Thread.sleep(rnd.nextInt(5) * 500);
		}

		sendEvent(topicTelemetria.name(), idSensor, converter.writeValueAsString(TelemetryEvent.down(idSensor)));
		sendEvent(topicLogger.name(), idSensor, String.format("%s INFO - Termina el sensor: %s",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), idSensor));
		System.exit(0);
	}

	ObjectMapper converter = new ObjectMapper();

//	@Scheduled(fixedDelay = 1000)
//	private void telemetria() throws JsonProcessingException {
//		sendEvent(topicTelemetria.name(), idSensor, converter.writeValueAsString(TelemetryEvent.up(idSensor)));
//	}

	@Autowired
	KafkaTemplate<String, TelemetryEvent> telemetriaTemplate;

	@Scheduled(fixedDelay = 1000)
	private void telemetria() throws JsonProcessingException {
		telemetriaTemplate.sendDefault(idSensor, TelemetryEvent.up(idSensor));
	}

	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;

	private void sendEvent(String topic, String origen, String value) {
		kafkaTemplate.send(topic, origen, value)
				.thenAccept(result -> System.out.println(String.format("TOPIC: %s KEY: %s VALUE: %s OFFSET: %s", topic, origen,
						value, result.getRecordMetadata().offset())))
				.exceptionally(ex -> {
					LOG.severe(String.format("TOPIC: %s, KEY: %s, VALUE: %s, ERROR: %s", topic, origen, value,
							ex.getMessage()));
					sendEvent(topicLogger.name(), idSensor, String.format(
							"ERROR - TOPIC: %s, KEY: %s, VALUE: %s, ERROR: %s", topic, origen, value, ex.getMessage()));
					return null;
				});
	}

}
