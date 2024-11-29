package com.example;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public 	record TelemetryEvent(String sensor, String estado, @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date enviado) {
	public static TelemetryEvent up(String sensor) {
		return new TelemetryEvent(sensor, "UP", new Date());
	}
	public static TelemetryEvent down(String sensor) {
		return new TelemetryEvent(sensor, "DOWN", new Date());
	}
}
