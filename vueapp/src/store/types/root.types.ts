import {CountryState} from '~/src/store/types/countries.types';
import {ProvinceState} from '~/src/store/types/provinces.types';

export interface RootState {
    countries: CountryState,
    provinces: ProvinceState
}
