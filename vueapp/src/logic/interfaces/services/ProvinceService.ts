import {Nullable} from '~/logic/Utils';
import {Province} from '~/logic/models/Province';

export interface ProvinceService {
    // TODO: Sin pais?
    list(): Promise<Province[]>;

    get(id: number): Promise<Nullable<Province>>;
}
