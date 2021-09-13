package com.c02.sensors.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.c02.sensors.model.SensorMeasurement;
import com.c02.sensors.repository.SensorRepository;

@ExtendWith(SpringExtension.class)
@Import(SensorServiceImpl.class)
public class SensorServiceTest {

	@Autowired
	private SensorService sensorService;

	@MockBean
	private SensorRepository sensorRepository;

	@Test
	public void shouldGetSensorMetrics() {
		Object[] array = new Object[2];
		array[0] = 1500;
		array[1] = 1300;
		Map<String, String> expectedMap = new HashMap<>();
		expectedMap.put("maxLast30Days", "1500");
		expectedMap.put("avgLast30Days", "1300");
		List<Object[]> sensorMetrics = new ArrayList<>();
		sensorMetrics.add(array);
		Mockito.when(sensorRepository.getSensorMetrics(Mockito.any(String.class), Mockito.any(Date.class),
				Mockito.any(Date.class))).thenReturn(sensorMetrics);
		Map<String, String> sensorMetricsMap = this.sensorService.getSensorMetrics(UUID.randomUUID().toString());
		assertThat(sensorMetricsMap, equalTo(expectedMap));
	}

	@Test
	public void shouldGetSensorAlerts() {
		String uUID = UUID.randomUUID().toString();
		List<SensorMeasurement> measurements = new ArrayList<>();
		Date oneDayago = DateUtils.addDays(new Date(), -1);
		Date twoDayago = DateUtils.addDays(new Date(), -2);
		SensorMeasurement measurement = getAlertMeasurements(uUID, oneDayago);
		measurements.add(measurement);
		measurement = getAlertMeasurements(uUID, twoDayago);
		measurements.add(measurement);
		Mockito.when(sensorRepository.findAllByuUIDAndStatus(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(measurements);
		Map<String, String> sensorAlertMap = this.sensorService.getSensorAlerts(uUID);
		assertThat(sensorAlertMap, equalTo(getExpectedAlertMap(oneDayago, twoDayago)));
	}
	
	@Test
	public void shouldGetSensorStatus() {
		String uUID = UUID.randomUUID().toString();
		SensorMeasurement measurement = getAlertMeasurements(uUID, new Date());
		Mockito.when(sensorRepository.findFirstByuUIDOrderByIdDesc(Mockito.any(String.class))).thenReturn(measurement);
		Map<String, String> sensorAlertMap = this.sensorService.getSensorStatus(uUID);
		Map<String, String> expectedStatusMap = new HashMap<>();
		expectedStatusMap.put("status", "ALERT");
		assertThat(sensorAlertMap, equalTo(expectedStatusMap));
	}
	

	private Map<String, String> getExpectedAlertMap(Date oneDayago, Date twoDayago) {
		Map<String, String> expectedMap = new HashMap<>();
		expectedMap.put("startTime", oneDayago.toString());
		expectedMap.put("endTime", twoDayago.toString());
		expectedMap.put("measurement1", "2200");
		expectedMap.put("measurement2", "2200");
		return expectedMap;
	}

	private SensorMeasurement getAlertMeasurements(String uUID, Date time) {
		SensorMeasurement measurement = new SensorMeasurement();
		measurement.setCo2(2200);
		measurement.setStatus("ALERT");
		measurement.setUUID(uUID);
		measurement.setTime(time);
		return measurement;
	}
}
