import {APIError} from '~/src/logic/models/APIError';
import {Workday} from '~/src/logic/models/Workday';
import {Nullable} from '~/src/logic/models/utils/Utils';

export const WorkdayMIME = {
    LIST: 'application/vnd.workday.list.get.v1+json',
    GET: 'application/vnd.workday.get.v1+json',
};

export interface WorkdayService {
    list(): Promise<Workday[] | APIError>;

    get(id: number): Promise<Nullable<Workday> | APIError>;

    delete(id: number): Promise<true | APIError>;
}
