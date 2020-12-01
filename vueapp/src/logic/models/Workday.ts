import {GenericEntity} from '~/src/logic/models/utils/GenericEntity';
import {JSONSerializableKeys} from '~/src/logic/models/utils/JSONSerializable';

export interface WorkdayHour {
    hour: number;
    minute: number;
}

export enum WorkdayDay {
    MONDAY = 'MONDAY',
    TUESDAY = 'TUESDAY',
    WEDNESDAY = 'WEDNESDAY',
    THURSDAY = 'THURSDAY',
    FRIDAY = 'FRIDAY',
    SATURDAY = 'SATURDAY',
    SUNDAY = 'SUNDAY'
}

export class Workday extends GenericEntity<Workday> {
    private _id: number;
    private _start: WorkdayHour;
    private _end: WorkdayHour;
    private _day: WorkdayDay;
    private _staffId: number;

    public get id(): number {
        return this._id;
    }

    public set id(value: number) {
        this._id = value;
    }

    public get start(): WorkdayHour {
        return this._start;
    }

    public set start(value: WorkdayHour) {
        this._start = value;
    }

    public get end(): WorkdayHour {
        return this._end;
    }

    public set end(value: WorkdayHour) {
        this._end = value;
    }

    public get day(): WorkdayDay {
        return this._day;
    }

    public set day(value: WorkdayDay) {
        this._day = value;
    }

    public get staffId(): number {
        return this._staffId;
    }

    public set staffId(value: number) {
        this._staffId = value;
    }

    public toJSON(): JSONSerializableKeys<Workday> {
        return {
            id: this.id,
            day: this.day,
            end: this.end,
            start: this.start,
            staffId: this.staffId
        };
    }
}
