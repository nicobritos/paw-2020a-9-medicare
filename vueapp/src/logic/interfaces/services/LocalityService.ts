import {Nullable} from '~/logic/Utils';
import {Locality} from '~/logic/models/Locality';
import {APIError} from '~/logic/models/APIError';

export interface LocalityListParams {
    countryId?: string,
    provinceId?: string
}

export interface LocalityService {
    list(params?: LocalityListParams): Promise<Nullable<Locality[]> | APIError>;

    get(id: number): Promise<Nullable<Locality>>;
}
