import Vue from 'vue';
import {CountryService} from '~/logic/interfaces/services/CountryService';
import container from '~/plugins/inversify.config';
import TYPES from '~/logic/types';
import {CountryActions, CountryMutations, countryMutationTypes, CountryState} from '~/store/types/countries.types';
import {Country} from '~/logic/models/Country';
import {DefineActionTree, DefineMutationTree} from '~/store/utils/helper.types';
import {RootState} from '~/store/types/root.types';
import {Module} from 'vuex';

function getService(): CountryService {
    return container.get(TYPES.Services.CountryService);
}

const state = (): CountryState => ({
    _listLoading: {
        loaded: false,
        promise: null
    },
    countries: [] as Array<Country>
});

const actions: DefineActionTree<CountryActions, CountryState, RootState> = {
    async getCountry({state, commit}, {payload}) {
        try {
            let data = await getService().get(payload.id);
            commit(countryMutationTypes.setCountry({data: data, id: payload.id}));
        } catch (e) {
            console.error(e);
        }
    },

    async loadCountries({state, commit}, {payload}) {
        if (state._listLoading.loaded) return;
        if (state._listLoading.promise) return;

        let promise = getService().list();
        commit(countryMutationTypes.setPromise(promise));

        let data: Country[] = [];
        try {
            data = await promise;
        } catch (e) {
            console.error(e);
        }
        commit(countryMutationTypes.setCountries(data));
    }
};

const mutations: DefineMutationTree<CountryMutations, CountryState> = {
    setCountry(state, {payload}): void {
        let index = state.countries.map(value => value.id).indexOf(payload.id);
        if (index >= 0) {
            if (payload.data) {
                Vue.set(state.countries, index, payload.data);
            } else {
                Vue.delete(state.countries, index);
            }
        } else if (payload.data) {
            state.countries.push(payload.data);
        }
    },
    setCountries(state, {payload}): void {
        state._listLoading.promise = null;
        state._listLoading.loaded = true;

        state.countries = payload;
    },
    setPromise(state, {payload}): void {
        state._listLoading.promise = payload;
    }
};

const store: Module<any, any> = {
    namespaced: true,
    actions,
    mutations,
    state
};

export default store;
