import {Nullable} from '~/src/logic/models/utils/Utils';
import {Province} from '~/src/logic/models/Province';

export interface ProvinceService {
    // TODO: Sin pais?
    list(): Promise<Province[]>;

    get(id: number): Promise<Nullable<Province>>;
}
