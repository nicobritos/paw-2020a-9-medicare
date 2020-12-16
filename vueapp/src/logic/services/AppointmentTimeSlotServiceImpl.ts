import {inject, injectable} from 'inversify';
import TYPES from '~/logic/types';
import {RestRepository} from '~/logic/interfaces/repositories/RestRepository';
import {APIError} from '~/logic/models/APIError';
import {DateRange} from '~/logic/models/utils/DateRange';
import {AppointmentTimeSlotService} from '~/logic/interfaces/services/AppointmentTimeSlotService';
import {AppointmentTimeslotDate} from '~/logic/models/AppointmentTimeslotDate';

const AppointmentTimeSlotMIME = {
    LIST: 'application/vnd.appointment-timeslot.list.get.v1+json',
};

@injectable()
export class AppointmentTimeSlotServiceImpl implements AppointmentTimeSlotService {
    private static PATH = 'appointmentTimeSlots';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    public async list(doctorId: number, dateRange: DateRange): Promise<AppointmentTimeslotDate[] | APIError> {
        let response = await this.rest.get<AppointmentTimeslotDate[]>(AppointmentTimeSlotServiceImpl.PATH, {
            accepts: AppointmentTimeSlotMIME.LIST
        });
        return response.response;
    }
}
