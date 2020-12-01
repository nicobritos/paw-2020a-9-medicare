import {Nullable} from '~/src/logic/models/utils/Utils';
import {Province} from '~/src/logic/models/Province';

export const ProvinceMIME = {
    LIST: 'application/vnd.province.list.get.v1+json',
    GET: 'application/vnd.province.get.v1+json',
};

export interface ProvinceService {
    // TODO: Sin pais?
    list(): Promise<Province[]>;

    get(id: number): Promise<Nullable<Province>>;
}
