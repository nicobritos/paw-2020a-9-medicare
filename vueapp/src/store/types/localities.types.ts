import {CacheableAsyncProperty, DefineTypes} from '~/store/utils/helper.types';
import {Locality} from '~/logic/models/Locality';

export interface LocalityState {
    localities: Locality[],
    _listLoading: CacheableAsyncProperty<Locality[]>
}

export interface LocalityActions {
    loadLocalities: void
}

export const localityActionTypes: DefineTypes<LocalityActions> = {
    loadLocalities: payload => ({payload, type: 'loadLocalities'})
};

export interface LocalityMutations {
    setPromise: Promise<Locality[]>,
    setLocalities: Locality[]
}

export const localityMutationTypes: DefineTypes<LocalityMutations> = {
    setPromise: payload => ({payload, type: 'setPromise'}),
    setLocalities: payload => ({payload, type: 'setLocalities'})
};
