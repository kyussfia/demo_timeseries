import React from 'react';

interface DatesComponentProps {
    selectedPowerStation: string;
    dates: string[];
    onDateSelect: (date: string) => void;
}

const DatesComponent: React.FC<DatesComponentProps> = ({ selectedPowerStation, dates, onDateSelect }) => {
    return (
        <div>
            <h2>Dates for {selectedPowerStation}</h2>
            <ul>
                {dates.map((date) => (
                    <li key={date} onClick={() => onDateSelect(date)}>
                        {date}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default DatesComponent;
