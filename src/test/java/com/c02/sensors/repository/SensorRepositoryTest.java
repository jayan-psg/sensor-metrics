package com.c02.sensors.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.c02.sensors.model.SensorMeasurement;

@DataJpaTest
public class SensorRepositoryTest {

	@Autowired
	private SensorRepository sensorRepository;

	@BeforeEach
	private void setUp() {
		this.sensorRepository.deleteAll();
	}

	@Test
	public void shouldCreateSensorMeasurement() {
		SensorMeasurement measurement = getSensorMeasureMent(1300, UUID.randomUUID().toString());
		SensorMeasurement saved = this.sensorRepository.save(measurement);
		SensorMeasurement fetched = this.sensorRepository.findById(saved.getId()).get();
		assertEquals(measurement.getUUID(), fetched.getUUID());
	}

	@Test
	public void shouldGetSensorMetrics() {
		String uUID = UUID.randomUUID().toString();
		this.sensorRepository.save(getSensorMeasureMent(1100, uUID));
		this.sensorRepository.save(getSensorMeasureMent(1500, uUID));
		Date today = new Date();
		List<Object[]> metrics = this.sensorRepository.getSensorMetrics(uUID, today, DateUtils.addDays(today, -30));
		for (Object[] obj : metrics) {
			assertEquals("1500", obj[0].toString());
			assertEquals("1300.0", obj[1].toString());
		}
	}

	@Test
	public void shouldGetSensorStatus() {
		String uUID = UUID.randomUUID().toString();
		SensorMeasurement sensorMeasurement = getSensorMeasureMent(1100, uUID);
		sensorMeasurement.setStatus("OK");
		this.sensorRepository.save(sensorMeasurement);
		SensorMeasurement measurement = this.sensorRepository.findFirstByuUIDOrderByIdDesc(uUID);
		assertEquals("OK", measurement.getStatus());
	}

	private SensorMeasurement getSensorMeasureMent(long level, String uUID) {
		SensorMeasurement sensorMeasurement = new SensorMeasurement();
		sensorMeasurement.setUUID(uUID);
		sensorMeasurement.setCo2(level);
		sensorMeasurement.setTime(DateUtils.addDays(new Date(), -2));
		return sensorMeasurement;
	}

}
