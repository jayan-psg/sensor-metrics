package com.c02.sensors.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.c02.sensors.model.SensorMeasurement;

@Repository
public interface SensorRepository extends JpaRepository<SensorMeasurement, Long> {
	@Query(value = "SELECT max(s.co2),avg(s.co2) FROM SensorMeasurement s where s.uUID = :uUID AND s.time < :startDate AND s.time > :endDate")
	public List<Object[]> getSensorMetrics(@Param("uUID") String uUID, @Param("startDate") Date startDate,
			@Param("endDate") Date endDate);

	public List<SensorMeasurement> findAllByuUIDAndStatus(String uUID,String status);

	public List<SensorMeasurement> findFirst2ByuUIDOrderByIdDesc(String uUID);
	
	public SensorMeasurement findFirstByuUIDOrderByIdDesc(String uUID);
}