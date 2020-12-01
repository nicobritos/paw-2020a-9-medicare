import {Nullable} from '~/src/logic/models/utils/Utils';
import {Province} from '~/src/logic/models/Province';
import {Locality} from '~/src/logic/models/Locality';

export const LocalityMIME = {
    LIST: 'application/vnd.locality.list.get.v1+json',
    GET: 'application/vnd.locality.get.v1+json',
};

export interface LocalityService {
    // TODO: Sin provincia?
    list(): Promise<Locality[]>;

    get(id: number): Promise<Nullable<Locality>>;
}
