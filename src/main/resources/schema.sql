-- -----------------------------------------------------
-- Table `time_series`.`greeting`
-- -----------------------------------------------------
CREATE TABLE greeting
(
    id       BIGINT       NOT NULL,
    language VARCHAR(255) NULL,
    text     VARCHAR(255) NULL,
    CONSTRAINT pk_greeting PRIMARY KEY (id)
);


-- -----------------------------------------------------
-- Table `time_series`.`time_series`
-- -----------------------------------------------------
CREATE TABLE time_series
(
    id            INTEGER NOT NULL,
    power_station VARCHAR(255) NOT NULL,
    date          VARCHAR(255) NOT NULL,
    zone          VARCHAR(255) NOT NULL,
    timestamp     VARCHAR(255) NOT NULL,
    period        VARCHAR (255) NOT NULL,
    series        VARCHAR(255) NOT NULL,
    version       INTEGER NOT NULL,
    CONSTRAINT pk_time_series PRIMARY KEY (id),
    CONSTRAINT u_time_series_power_station_date_version UNIQUE(power_station, date, version)
);