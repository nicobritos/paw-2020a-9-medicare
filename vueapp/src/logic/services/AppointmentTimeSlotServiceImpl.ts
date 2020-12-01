import {inject, injectable} from 'inversify';
import TYPES from '~/src/logic/types';
import {RestRepository} from '~/src/logic/interfaces/repositories/RestRepository';
import {APIError} from '~/src/logic/models/APIError';
import {DateRange} from '~/src/logic/models/utils/DateRange';
import {AppointmentTimeSlotService} from '~/src/logic/interfaces/services/AppointmentTimeSlotService';
import {AppointmentTimeslotDate} from '~/src/logic/models/AppointmentTimeslotDate';

const AppointmentTimeSlotMIME = {
    LIST: 'application/vnd.appointment-timeslot.list.get.v1+json',
};

@injectable()
export class AppointmentTimeSlotServiceImpl implements AppointmentTimeSlotService {
    private static PATH = 'appointmentTimeSlots';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    public async list(staffId: number, dateRange: DateRange): Promise<AppointmentTimeslotDate[] | APIError> {
        let response = await this.rest.get<AppointmentTimeslotDate[]>(AppointmentTimeSlotServiceImpl.PATH, {
            accepts: AppointmentTimeSlotMIME.LIST
        });
        return response.isOk() ? response.data! : response.error!;
    }
}
