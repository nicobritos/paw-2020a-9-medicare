import {Province} from '~/src/logic/models/Province';
import {CacheableAsyncProperty, DefineTypes} from '~/src/store/utils/helper.types';
import {Nullable} from '~/src/logic/models/utils/Utils';

export interface ProvinceStateType {
    [countryId: string]: Array<Province>;
    [countryId: number]: Array<Province>;
}

export interface ProvincePromiseStateType {
    [countryId: string]: CacheableAsyncProperty<Array<Province>>;
    [countryId: number]: CacheableAsyncProperty<Array<Province>>;
}

export interface ProvinceState {
    provinces: ProvinceStateType,
    _listLoading: ProvincePromiseStateType
}

export interface ProvinceActions {
    loadProvinces: {
        countryId: string
    },
    getProvince: {
        id: number
    }
}

export interface ProvinceActionReturnTypes {
    getProvince: Nullable<Province>;
}

export const provinceActionTypes: DefineTypes<ProvinceActions> = {
    getProvince: payload => ({payload, type: 'getProvince'}),
    loadProvinces: payload => ({payload, type: 'loadProvinces'})
};

export interface ProvinceMutations {
    setProvinces: { countryId: string, data: Array<Province> },
    setProvince: { countryId?: string, id: number, data: Nullable<Province> },
    setPromise: { countryId: string, promise: Promise<Array<Province>> }
}

export const provinceMutationTypes: DefineTypes<ProvinceMutations> = {
    setPromise: payload => ({payload, type: 'setPromise'}),
    setProvince: payload => ({payload, type: 'setProvince'}),
    setProvinces: payload => ({payload, type: 'setProvinces'})
};
