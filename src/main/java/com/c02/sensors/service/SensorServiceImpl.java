package com.c02.sensors.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c02.sensors.constants.ApplicationConstants;
import com.c02.sensors.exception.NotFoundException;
import com.c02.sensors.model.SensorMeasurement;
import com.c02.sensors.repository.SensorRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SensorServiceImpl implements SensorService {

	@Autowired
	private SensorRepository sensorRepository;

	private static final long threshold = 2000;

	Map<String, String> sensorStatus = new HashMap<>();

	@Override
	public void addSensorMeasurement(SensorMeasurement sensor) {
		setSensorStatus(sensor);
		this.sensorRepository.save(sensor);
		log.info("Added CO2 measurement for the sensor {}", sensor.getUUID());
	}
	/**
	 * Finds the status of sensor based on the CO2 level and previous status
	 * @param current
	 */
	private void setSensorStatus(SensorMeasurement current) {
		List<SensorMeasurement> sensors = this.sensorRepository.findFirst2ByuUIDOrderByIdDesc(current.getUUID());
		AtomicInteger checkAlertCounter = new AtomicInteger(0);
		sensors.forEach(sensor -> {
			if (sensor.getCo2() > threshold)
				checkAlertCounter.incrementAndGet();
		});
		if (checkAlertCounter.get() < 2 && current.getCo2() > threshold)
			current.setStatus(ApplicationConstants.WARN);
		else if (checkAlertCounter.get() != 0 && (current.getCo2() > threshold || current.getCo2() < threshold))
			current.setStatus(ApplicationConstants.ALERT);
		else
			current.setStatus(ApplicationConstants.OK);
	}

	@Override
	public Map<String, String> getSensorMetrics(String uUID) {
		Date today = new Date();
		Date dateBefore30Days = DateUtils.addDays(today, -30);
		List<Object[]> sensorData = sensorRepository.getSensorMetrics(uUID, today, dateBefore30Days);
		Map<String, String> sensorDataMap = new HashMap<>();
		for (Object[] obj : sensorData) {
			if (obj[0] != null && obj[1] != null) {
				sensorDataMap.put("maxLast30Days", obj[0].toString());
				sensorDataMap.put("avgLast30Days", obj[1].toString());
			} else {
				throw new NotFoundException("Metrics are not available.");
			}
		}
		log.info("Got metrics {} for sensor {}", sensorDataMap, uUID);
		return sensorDataMap;
	}

	@Override
	public Map<String, String> getSensorAlerts(String uUID) {
		List<SensorMeasurement> sensors = this.sensorRepository.findAllByuUIDAndStatus(uUID,
				ApplicationConstants.ALERT);
		if (sensors.isEmpty()) {
			throw new NotFoundException("Alerts are not available for " + uUID);
		}
		Map<String, String> sensorAlertMap = new HashMap<>();
		sensors.stream().findFirst().ifPresent(sens -> {
			sensorAlertMap.put("startTime", sens.getTime().toString());
		});
		sensors.stream().reduce((a, b) -> b).ifPresent(sens -> {
			sensorAlertMap.put("endTime", sens.getTime().toString());
		});
		AtomicInteger index = new AtomicInteger(0);
		sensors.forEach(sens -> {
			sensorAlertMap.put("measurement" + index.incrementAndGet(), String.valueOf(sens.getCo2()));
		});
		log.info("Got alerts {} for sensor {}", sensorAlertMap, uUID);
		return sensorAlertMap;
	}

	@Override
	public Map<String, String> getSensorStatus(String uUID) {
		Map<String, String> sensorStatus = new HashMap<>();
		SensorMeasurement sensor = this.sensorRepository.findFirstByuUIDOrderByIdDesc(uUID);
		if (sensor == null)
			throw new NotFoundException("Sensor status is not available for " + uUID);
		sensorStatus.put("status", sensor.getStatus());
		log.info("Sensor status for {} is {}", uUID, sensorStatus);
		return sensorStatus;
	}
}