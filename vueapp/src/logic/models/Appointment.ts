import {GenericEntity} from '~/src/logic/models/utils/GenericEntity';
import {JSONSerializableKeys} from '~/src/logic/models/utils/JSONSerializable';

export enum AppointmentStatus {
    PENDING= 'PENDING',
    COMPLETE = 'COMPLETE',
    CANCELLED = 'CANCELLED',
    WAITING = 'WAITING',
    SEEN = 'SEEN'
}

export class Appointment extends GenericEntity<Appointment> {
    private _id: number;
    private _status: AppointmentStatus;
    private _dateFrom: Date;
    private _message: string;
    private _motive: string;
    private _patientId: number;
    private _staffId: number;

    public get id(): number {
        return this._id;
    }

    public set id(value: number) {
        this._id = value;
    }

    public get status(): AppointmentStatus {
        return this._status;
    }

    public set status(value: AppointmentStatus) {
        this._status = value;
    }

    public get dateFrom(): Date {
        return this._dateFrom;
    }

    public set dateFrom(value: Date) {
        this._dateFrom = value;
    }

    public get message(): string {
        return this._message;
    }

    public set message(value: string) {
        this._message = value;
    }

    public get motive(): string {
        return this._motive;
    }

    public set motive(value: string) {
        this._motive = value;
    }

    public get patientId(): number {
        return this._patientId;
    }

    public set patientId(value: number) {
        this._patientId = value;
    }

    public get staffId(): number {
        return this._staffId;
    }

    public set staffId(value: number) {
        this._staffId = value;
    }

    public toJSON(): JSONSerializableKeys<Appointment> {
        return {
            id: this.id,
            dateFrom: this.dateFrom,
            message: this.message,
            motive: this.motive,
            patientId: this.patientId,
            staffId: this.staffId,
            status: this.status
        };
    }
}
