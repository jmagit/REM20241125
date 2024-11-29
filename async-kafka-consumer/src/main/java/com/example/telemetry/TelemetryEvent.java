package com.example.telemetry;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public 	record TelemetryEvent(String sensor, String estado, @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss") Date enviado) {
}
