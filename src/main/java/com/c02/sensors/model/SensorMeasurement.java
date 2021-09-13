package com.c02.sensors.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class SensorMeasurement {
	@Id
	@GeneratedValue
	private long id;
	private String uUID;
	private long co2;
	private Date time;
	private String status;
}