import {JSONSerializable, JSONSerializableKeys} from '~/logic/models/utils/JSONSerializable';
import {AppointmentTimeslot} from '~/logic/models/AppointmentTimeslot';

export interface TimeslotDate {
    hour: number;
    month: number;
    year: number;
}

export class AppointmentTimeslotDate implements JSONSerializable<AppointmentTimeslotDate> {
    private _date: TimeslotDate;
    private _timeslots: AppointmentTimeslot[];

    public get date(): TimeslotDate {
        return this._date;
    }

    public set date(value: TimeslotDate) {
        this._date = value;
    }

    public get timeslots(): AppointmentTimeslot[] {
        return this._timeslots;
    }

    public set timeslots(value: AppointmentTimeslot[]) {
        this._timeslots = value;
    }

    public toJSON(): JSONSerializableKeys<AppointmentTimeslotDate> {
        return {
            date: this.date,
            timeslots: this.timeslots
        };
    }
}
