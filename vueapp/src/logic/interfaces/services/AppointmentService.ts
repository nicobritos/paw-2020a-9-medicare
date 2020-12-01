import {APIError} from '~/src/logic/models/APIError';
import {Nullable} from '~/src/logic/models/utils/Utils';
import {Appointment} from '~/src/logic/models/Appointment';
import {DateRange} from '~/src/logic/models/utils/DateRange';

export const AppointmentMIME = {
    LIST: 'application/vnd.appointment.list.get.v1+json',
    GET: 'application/vnd.appointment.get.v1+json',
    CREATE: 'application/vnd.appointment.create.v1+json'
};

export interface CreateAppointment {
    date_from: Date;
    message?: string;
    motive?: string;
    staffId: number;
}

export interface UserService {
    list(dateRange: DateRange): Promise<Appointment[]>;

    get(id: number): Promise<Nullable<Appointment>>;

    create(appointment: CreateAppointment): Promise<Appointment | APIError>;

    delete(id: number): Promise<true | APIError>
}
