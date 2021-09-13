package com.c02.sensors.service;

import java.util.Map;

import com.c02.sensors.model.SensorMeasurement;

public interface SensorService {
	public void addSensorMeasurement(SensorMeasurement sensor);
	public Map<String, String> getSensorMetrics(String uUID);
	public Map<String, String> getSensorAlerts(String uUID);
	public Map<String, String> getSensorStatus(String uUID);
}