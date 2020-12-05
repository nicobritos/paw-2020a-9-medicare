import {Nullable} from '~/logic/models/utils/Utils';
import {Locality} from '~/logic/models/Locality';

export interface LocalityService {
    // TODO: Sin provincia?
    list(): Promise<Locality[]>;

    get(id: number): Promise<Nullable<Locality>>;
}
