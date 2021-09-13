package com.c02.sensors.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.c02.sensors.model.SensorMeasurement;
import com.c02.sensors.service.SensorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SensorController.class)
public class SensorControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private SensorService sensorService;

	@Test
	public void shouldAddMeasurements() throws Exception {
		String json = "{\r\n" + "\"co2\" : 2150,\r\n" + "\"time\" : \"2021-09-05T18:55:47+00:00\"\r\n" + "}";
		Mockito.doNothing().when(sensorService).addSensorMeasurement(Mockito.any(SensorMeasurement.class));
		mockMvc.perform(post("/api/v1/sensors/321/mesurements").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isCreated());
	}

	@Test
	public void shouldGetStatusofSensor() throws Exception {
		Map<String, String> sensorStatus = new HashMap<>();
		sensorStatus.put("status", "OK");
		Mockito.when(sensorService.getSensorStatus("123e4567-e89b-42d3-a456-556642440000")).thenReturn(sensorStatus);
		mockMvc.perform(
				get("/api/v1/sensors/123e4567-e89b-42d3-a456-556642440000").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().json("{\"status\":\"OK\"}"));
	}

	@Test
	public void shouldGetAllAlerts() throws Exception {
		Map<String, String> alertMap = new HashMap<>();
		alertMap.put("startTime", new Date().toString());
		alertMap.put("endTime", new Date().toString());
		alertMap.put("measurement1", "2500");
		alertMap.put("measurement2", "2900");
		Mockito.when(sensorService.getSensorAlerts("123e4567-e89b-42d3-a456-556642440000")).thenReturn(alertMap);
		mockMvc.perform(get("/api/v1/sensors/123e4567-e89b-42d3-a456-556642440000/alerts")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void shouldGetSensorMetrics() throws Exception {
		Map<String, String> metricsMap = new HashMap<>();
		metricsMap.put("maxLast30Days", "1200");
		metricsMap.put("avgLast30Days", "900");
		Mockito.when(sensorService.getSensorMetrics("123e4567-e89b-42d3-a456-556642440000")).thenReturn(metricsMap);
		mockMvc.perform(get("/api/v1/sensors/321/metrics")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}
