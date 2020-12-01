import {DateRange} from '~/src/logic/models/utils/DateRange';
import {AppointmentTimeslotDate} from '~/src/logic/models/AppointmentTimeslotDate';
import {APIError} from '~/src/logic/models/APIError';

export const AppointmentTimeSlotMIME = {
    LIST: 'application/vnd.appointment-timeslot.list.get.v1+json',
};

export interface AppointmentTimeSlotService {
    list(staffId: number, dateRange: DateRange): Promise<AppointmentTimeslotDate[] | APIError>;
}
