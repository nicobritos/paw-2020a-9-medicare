import {CacheableAsyncProperty, DefineTypes} from '~/src/store/utils/helper.types';
import {Country} from '~/src/logic/models/Country';
import {Nullable} from '~/src/logic/models/utils/Utils';

export interface CountryState {
    countries: Array<Country>,
    _listLoading: CacheableAsyncProperty<Array<Country>>,
}

export interface CountryActions {
    loadCountries: {}
    getCountry: {
        id: string
    }
}

export const countryActionTypes: DefineTypes<CountryActions> = {
    getCountry: payload => ({payload, type: 'getCountry'}),
    loadCountries: payload => ({payload, type: 'loadCountries'})
};

export interface CountryMutations {
    setCountries: CountryState['countries'],
    setCountry: { id: string, data: Nullable<Country> },
    setPromise: CountryState['_listLoading']['promise']
}

export const countryMutationTypes: DefineTypes<CountryMutations> = {
    setCountry: payload => ({payload, type: 'setCountry'}),
    setCountries: payload => ({payload, type: 'setCountries'}),
    setPromise: payload => ({payload, type: 'setPromise'})
};
