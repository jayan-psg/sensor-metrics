package com.c02.sensors.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.c02.sensors.model.SensorMeasurement;
import com.c02.sensors.service.SensorService;

import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
public class SensorController {

	@Autowired
	private SensorService sensorService;

	@PostMapping(value = "/api/v1/sensors/{uuid}/mesurements")
	public ResponseEntity<SensorMeasurement> addSensorMeasurements(@RequestBody SensorMeasurement sensor,
			@PathVariable String uuid) {
		sensor.setUUID(uuid);
		this.sensorService.addSensorMeasurement(sensor);
		return new ResponseEntity<SensorMeasurement>(HttpStatus.CREATED);
	}

	@GetMapping(value = "/api/v1/sensors/{uuid}/metrics")
	public ResponseEntity<Object> getSensorMetrics(@PathVariable String uuid) {
		return new ResponseEntity<Object>(sensorService.getSensorMetrics(uuid), HttpStatus.OK);
	}

	@GetMapping(value = "/api/v1/sensors/{uuid}/alerts")
	public ResponseEntity<Object> getSensorAlerts(@PathVariable String uuid) {
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
		listMap.add(this.sensorService.getSensorAlerts(uuid));
		return new ResponseEntity<Object>(listMap, HttpStatus.OK);
	}

	@GetMapping(value = "/api/v1/sensors/{uuid}")
	public ResponseEntity<Object> getSensorStatus(@PathVariable String uuid) {
		return ResponseEntity.status(HttpStatus.OK).body(this.sensorService.getSensorStatus(uuid));
	}
}