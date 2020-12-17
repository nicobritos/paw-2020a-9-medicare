import {Nullable} from '~/logic/Utils';
import {Locality} from '~/logic/models/Locality';
import {APIError} from '~/logic/models/APIError';

export interface LocalityService {
    list(provinceId?: number): Promise<Nullable<Locality[]> | APIError>;

    get(id: number): Promise<Nullable<Locality>>;
}
