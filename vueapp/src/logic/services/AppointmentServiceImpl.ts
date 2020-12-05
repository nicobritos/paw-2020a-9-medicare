import {Nullable} from '~/logic/models/utils/Utils';
import {inject, injectable} from 'inversify';
import TYPES from '~/logic/types';
import {RestRepository} from '~/logic/interfaces/repositories/RestRepository';
import {getPathWithId} from '~/logic/services/Utils';
import {AppointmentService, CreateAppointment} from '~/logic/interfaces/services/AppointmentService';
import {Appointment} from '~/logic/models/Appointment';
import {APIError} from '~/logic/models/APIError';
import {DateRange} from '~/logic/models/utils/DateRange';

const AppointmentMIME = {
    LIST: 'application/vnd.appointment.list.get.v1+json',
    GET: 'application/vnd.appointment.get.v1+json',
    CREATE: 'application/vnd.appointment.create.v1+json'
};

@injectable()
export class AppointmentServiceImpl implements AppointmentService {
    private static PATH = 'appointments';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    public async get(id: number): Promise<Nullable<Appointment>> {
        let response = await this.rest.get<Appointment>(getPathWithId(AppointmentServiceImpl.PATH, id), {
            accepts: AppointmentMIME.GET
        });
        return response.isOk() ? response.data! : null;
    }

    public async list(dateRange: DateRange): Promise<Appointment[]> {
        let response = await this.rest.get<Appointment[]>(AppointmentServiceImpl.PATH, {
            accepts: AppointmentMIME.LIST
        });
        return response.isOk() ? response.data! : [];
    }

    public async create(appointment: CreateAppointment): Promise<Appointment | APIError> {
        let response = await this.rest.post<Appointment, CreateAppointment>(AppointmentServiceImpl.PATH, {
            accepts: AppointmentMIME.GET,
            data: appointment,
            contentType: AppointmentMIME.CREATE
        });
        return response.isOk() ? response.data! : response.error!;
    }

    public async delete(id: number): Promise<true | APIError> {
        let response = await this.rest.delete(getPathWithId(AppointmentServiceImpl.PATH, id));
        return response.isOk() ? true : response.error!;
    }
}
