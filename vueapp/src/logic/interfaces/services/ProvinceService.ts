import {Nullable} from '~/logic/Utils';
import {Province} from '~/logic/models/Province';
import {APIError} from '~/logic/models/APIError';

export interface ProvinceService {
    list(countryId?: string): Promise<Nullable<Province[]> | APIError>;

    get(id: number): Promise<Nullable<Province>>;
}
