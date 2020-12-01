import {Country} from '~/src/logic/models/Country';
import {Nullable} from '~/src/logic/models/utils/Utils';

export interface CountryService {
    list(): Promise<Country[]>;

    // TODO: Update api.json to id string
    get(id: string): Promise<Nullable<Country>>;
}
