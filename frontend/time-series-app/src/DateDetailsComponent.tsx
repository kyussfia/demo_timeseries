import React from 'react';
import { TimeSeries } from './TimeSeries';
interface DateDetailsProps {
    selectedPowerStation: string;
    selectedDate: string;
    data: TimeSeries[];
}

const DateDetailsComponent: React.FC<DateDetailsProps> = ({ selectedPowerStation, selectedDate, data }) => {
    const renderTableHeader = () => {
        if (data.length === 0) {
            return null;
        }

        const headers = [];
        headers.push(<th key="period-end">Period end</th>);

        for (let i = 0; i < data.length; i++) {
            headers.push(<th key={`ver${i}`}>ver{data[i].version}</th>);
        }

        return <tr>{headers}</tr>;
    };

    const renderSecondaryTableHeader = () => {
        const headers = [];
        headers.push(<td key="placeholder"></td>);

        for (let i = 0; i < data.length; i++) {
            headers.push(<td key={`timestamp${i}`}>{data[i].timestamp}</td>);
        }

        return headers;
    };

    const renderTableRows = () => {
        const period = data.length > 0 ? data[0].period : '';
        const timeIntervals = period ? generateTimeIntervals(period) : [];

        const rows = [];
        rows.push(renderSecondaryTableHeader())
        for (let i = 0; i < timeIntervals.length; i++) {
            const row = [];
            row.push(<td key={`interval${i}`}>{timeIntervals[i]}</td>);

            for (let j = 0; j < data.length; j++) {
                row.push(
                    <td key={`ver${data[j].version}_series_${i}`}>
                        {data[j].series[i] === 0 ? 'N/A' : data[j].series[i]}
                    </td>
                );
            }

            rows.push(<tr key={`row${i}`}>{row}</tr>);
        }

        return rows;
    };

    const parseDuration = (durationString: string) => {
        const match = durationString.match(/^([A-Za-z]+)(\d+)([A-Za-z])$/);
        if (!match) {
            return null;
        }

        const [, prefix, value, unit] = match;
        return { prefix, value: parseInt(value), unit };
    };

    const generateTimeIntervals = (period: string) => {

        const duration = parseDuration(period)
        const interval: number = duration?.value ? duration.value : 15;

        const timeIntervals = [];
        for (let hour = 0; hour < 24; hour++) {
            for (let minute = 0; minute < 60; minute += interval) {
                const formattedHour = hour.toString().padStart(2, '0');
                const formattedMinute = minute.toString().padStart(2, '0');
                timeIntervals.push(`${formattedHour}:${formattedMinute}`);
            }
        }

        return timeIntervals;
    };

    return (
        <div>
            <h2>Date Details for {selectedPowerStation} at {selectedDate}</h2>
            <table>
                <thead>{renderTableHeader()}</thead>
                <tbody>{renderTableRows()}</tbody>
            </table>
        </div>
    );
};

export default DateDetailsComponent;
