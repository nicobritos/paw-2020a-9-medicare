import {CountryState} from '~/store/types/countries.types';
import {ProvinceState} from '~/store/types/provinces.types';

export interface RootState {
    countries: CountryState,
    provinces: ProvinceState
}
