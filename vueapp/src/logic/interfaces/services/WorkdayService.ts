import {APIError} from '~/logic/models/APIError';
import {Workday} from '~/logic/models/Workday';
import {Nullable} from '~/logic/models/utils/Utils';

export interface WorkdayService {
    list(): Promise<Workday[] | APIError>;

    get(id: number): Promise<Nullable<Workday> | APIError>;

    delete(id: number): Promise<true | APIError>;
}
