package com.c02.sensors.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.c02.sensors.model.SensorMeasurement;
import com.c02.sensors.repository.SensorRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SensorControllerIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private SensorRepository sensorRepository;
	
	@BeforeEach
	private void clearAll() {
		sensorRepository.deleteAll();
	}

	@Test
	public void shouldAddMeasurement() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<SensorMeasurement> entity = new HttpEntity<SensorMeasurement>(getMeasurement(1300), headers);
		ResponseEntity<String> response = restTemplate
				.postForEntity(createURLWithPort("/api/v1/sensors/321/mesurements"), entity, String.class);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	@Test
	public void shouldReturnSensorMetrics() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<SensorMeasurement> entity = new HttpEntity<SensorMeasurement>(getMeasurement(1300), headers);
		restTemplate
				.postForEntity(createURLWithPort("/api/v1/sensors/321/mesurements"), entity, String.class);
		
		
		entity = new HttpEntity<SensorMeasurement>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/sensors/321/metrics"),
				HttpMethod.GET, entity, String.class);

		String expected = "{\"maxLast30Days\":\"1300\",\"avgLast30Days\":\"1300.0\"}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void shouldReturnStatusOKWithCO2LessThan2000() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<SensorMeasurement> entity = new HttpEntity<SensorMeasurement>(getMeasurement(1300), headers);
		restTemplate
				.postForEntity(createURLWithPort("/api/v1/sensors/321/mesurements"), entity, String.class);
		
		entity = new HttpEntity<SensorMeasurement>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/sensors/321"), HttpMethod.GET,
				entity, String.class);

		String expected = "{\"status\":\"OK\"}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	
	@Test
	public void shouldReturnStatusWARNwithCO2GreaterThan2000() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<SensorMeasurement> entity = new HttpEntity<SensorMeasurement>(getMeasurement(2300), headers);
		restTemplate
				.postForEntity(createURLWithPort("/api/v1/sensors/321/mesurements"), entity, String.class);
		
		entity = new HttpEntity<SensorMeasurement>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/sensors/321"), HttpMethod.GET,
				entity, String.class);

		String expected = "{\"status\":\"WARN\"}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}
	
	@Test
	public void shouldReturnStatusALERTwithCO2GreaterThan2000() throws JSONException {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<SensorMeasurement> entity = new HttpEntity<SensorMeasurement>(getMeasurement(2300), headers);
		restTemplate
				.postForEntity(createURLWithPort("/api/v1/sensors/321/mesurements"), entity, String.class);
		
		entity = new HttpEntity<SensorMeasurement>(getMeasurement(2300), headers);
		restTemplate
				.postForEntity(createURLWithPort("/api/v1/sensors/321/mesurements"), entity, String.class);

		entity = new HttpEntity<SensorMeasurement>(getMeasurement(2300), headers);
		restTemplate
				.postForEntity(createURLWithPort("/api/v1/sensors/321/mesurements"), entity, String.class);
		
		
		entity = new HttpEntity<SensorMeasurement>(null, headers);
		ResponseEntity<String> response = restTemplate.exchange(createURLWithPort("/api/v1/sensors/321"), HttpMethod.GET,
				entity, String.class);

		String expected = "{\"status\":\"ALERT\"}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}	
	

	private SensorMeasurement getMeasurement(long level) {
		SensorMeasurement measurement = new SensorMeasurement();
		measurement.setCo2(level);
		measurement.setTime(new Date());
		return measurement;
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

}
