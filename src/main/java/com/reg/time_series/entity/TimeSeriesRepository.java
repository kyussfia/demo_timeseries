package com.reg.time_series.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeSeriesRepository extends JpaRepository<TimeSeries, Integer> {

    TimeSeries findFirstByPowerStationAndDateOrderByVersionDesc(String powerStation, String date);

    List<TimeSeries> findByPowerStationAndDateOrderByVersionAsc(String powerStation, String date);

    @Query("SELECT DISTINCT t.powerStation FROM TimeSeries t")
    List<String> findAllDistinctPowerStations();

    @Query("SELECT DISTINCT t.date FROM TimeSeries t WHERE t.powerStation = :powerStation")
    List<String> findDistinctDatesByPowerStation(String powerStation);
}
