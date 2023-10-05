package com.reg.time_series.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reg.time_series.IntegerArrayConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

@Entity
@Table(name = "time_series", schema = "main")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimeSeries {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("power-station")
    @Column(name = "power_station", nullable = false)
    private String powerStation;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "zone", nullable = false)
    private String zone;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "period", nullable = false)
    private String period;

    @Column(name = "series", nullable = false)
    @Convert(converter = IntegerArrayConverter.class)
    private Integer[] series;

    @Column(name = "version", nullable = false)
    private Integer version;

    @PrePersist
    public void prePersist() {
        if (null == this.version) {
            this.version = 1;
        } else {
            this.version++;
        }
    }

    public TimeSeries merge(TimeSeries latest, Duration safetyWindow) {
        final Duration period = Duration.parse(this.period);
        final LocalDateTime latestProtectedUntil = LocalDateTime.parse(latest.getTimestamp(), DATE_FORMATTER).plus(safetyWindow);
        LocalDateTime counter = LocalDate.parse(this.date).atStartOfDay();
        ArrayList<Integer> merged = new ArrayList<>();
        for (int i = 0; i < this.series.length; i++) {
            if (counter.isBefore(latestProtectedUntil) || counter.isEqual(latestProtectedUntil)) {
                merged.add(latest.getSeries()[i]);
            } else {
                merged.add(this.series[i]);
            }
            counter = counter.plus(period);
        }

        this.version = latest.getVersion();
        this.series = merged.toArray(new Integer[]{});

        return this;
    }

    @Override
    public String toString() {
        return "TimeSeries{" +
                "id=" + id +
                ", powerStation='" + powerStation + '\'' +
                ", date='" + date + '\'' +
                ", zone='" + zone + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", period='" + period + '\'' +
                ", series=" + Arrays.toString(series) +
                ", version=" + version +
                '}';
    }
}
