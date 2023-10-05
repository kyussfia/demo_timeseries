export interface TimeSeries {
    id: number;
    date: string;
    zone: string;
    timestamp: string;
    period: string;
    series: number[];
    version: number;
    periodAsDuration: number;
    'power-station': string;
}