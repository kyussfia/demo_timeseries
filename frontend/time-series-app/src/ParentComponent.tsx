import React, { useState, useEffect } from 'react';
import PowerStationsComponent from './PowerStationsComponent';
import DatesComponent from './DatesComponent';
import DateDetailsComponent from './DateDetailsComponent';
import { TimeSeries } from './TimeSeries';
import axios from 'axios';

const backendUrl = 'http://localhost:8080/api';

const ParentComponent = () => {
    const [powerStations, setPowerStations] = useState([]);
    const [selectedPowerStation, setSelectedPowerStation] = useState('');
    const [dates, setDates] = useState([]);
    const [selectedDate, setSelectedDate] = useState('');
    const [details, setDetails ] = useState<TimeSeries[]>([]);

    useEffect(() => {
        axios.get(`${backendUrl}/time-series/power-stations`).then((response) => {
            setPowerStations(response.data);
        });
    }, []);

    const handlePowerStationSelect = (station: string) => {
        setSelectedPowerStation(station);
        setSelectedDate(''); // Reset selected date when power station changes
        axios.get(`${backendUrl}/time-series/power-stations/${station}/dates`).then((response) => {
            setDates(response.data);
        });
    };

    const handleDateSelect = (date: string) => {
        setSelectedDate(date);
        setDetails([]);
        axios.get<TimeSeries[]>(`${backendUrl}/time-series/power-stations/${selectedPowerStation}/dates/${date}`).then((response) => {
            setDetails(response.data);
        });

    };

    return (
        <div>
            <PowerStationsComponent powerStations={powerStations} onPowerStationSelect={handlePowerStationSelect} />
            {selectedPowerStation && (
                <DatesComponent selectedPowerStation={selectedPowerStation} dates={dates} onDateSelect={handleDateSelect} />
            )}
            {selectedDate && (
                <DateDetailsComponent selectedPowerStation={selectedPowerStation} selectedDate={selectedDate} data={details} />
            )}
        </div>
    );
};

export default ParentComponent;
