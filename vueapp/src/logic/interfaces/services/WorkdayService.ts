import {APIError} from '~/src/logic/models/APIError';
import {Workday} from '~/src/logic/models/Workday';
import {Nullable} from '~/src/logic/models/utils/Utils';

export interface WorkdayService {
    list(): Promise<Workday[] | APIError>;

    get(id: number): Promise<Nullable<Workday> | APIError>;

    delete(id: number): Promise<true | APIError>;
}
