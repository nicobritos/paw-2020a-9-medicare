import {Country} from '~/src/logic/models/Country';
import {Nullable} from '~/src/logic/models/utils/Utils';

export const CountryMIME = {
    LIST: 'application/vnd.country.list.get.v1+json',
    GET: 'application/vnd.country.get.v1+json',
};

export interface CountryService {
    list(): Promise<Country[]>;

    // TODO: Update api.json to id string
    get(id: string): Promise<Nullable<Country>>;
}
