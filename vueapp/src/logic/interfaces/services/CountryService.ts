import {Country} from '~/logic/models/Country';
import {Nullable} from '~/logic/models/utils/Utils';

export interface CountryService {
    list(): Promise<Country[]>;

    // TODO: Update api.json to id string
    get(id: string): Promise<Nullable<Country>>;
}
