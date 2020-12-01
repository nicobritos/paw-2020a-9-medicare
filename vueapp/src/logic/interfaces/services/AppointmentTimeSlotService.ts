import {DateRange} from '~/src/logic/models/utils/DateRange';
import {AppointmentTimeslotDate} from '~/src/logic/models/AppointmentTimeslotDate';
import {APIError} from '~/src/logic/models/APIError';

export interface AppointmentTimeSlotService {
    list(staffId: number, dateRange: DateRange): Promise<AppointmentTimeslotDate[] | APIError>;
}
