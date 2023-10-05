import React from 'react';

interface PowerStationsComponentProps {
    powerStations: string[];
    onPowerStationSelect: (station: string) => void;
}

const PowerStationsComponent: React.FC<PowerStationsComponentProps> = ({ powerStations, onPowerStationSelect }) => {
    return (
        <div>
            <h2>Power Stations</h2>
            <ul>
                {powerStations.map((station) => (
                    <li key={station} onClick={() => onPowerStationSelect(station)}>
                        {station}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default PowerStationsComponent;
