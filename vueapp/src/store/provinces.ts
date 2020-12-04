import {ProvinceService} from '~/src/logic/interfaces/services/ProvinceService';
import container from '~/src/plugins/inversify.config';
import TYPES from '~/src/logic/types';
import {
    ProvinceActionReturnTypes,
    ProvinceActions, ProvinceMutations, provinceMutationTypes,
    ProvincePromiseStateType,
    ProvinceState,
    ProvinceStateType,
} from '~/src/store/types/provinces.types';
import {DefineActionTree, DefineMutationTree} from '~/src/store/utils/helper.types';
import {RootState} from '~/src/store/types/root.types';
import {Nullable} from '~/src/logic/models/utils/Utils';
import {Province} from '~/src/logic/models/Province';
import Vue from 'vue';
import {Module} from 'vuex';

function getService(): ProvinceService {
    return container.get(TYPES.Services.ProvinceService);
}

export const state = (): ProvinceState => ({
    _listLoading: {} as ProvincePromiseStateType,
    provinces: {} as ProvinceStateType
});

export const actions: DefineActionTree<ProvinceActions, ProvinceState, RootState, ProvinceActionReturnTypes> = {
    async getProvince({state, commit}, {payload}): Promise<Nullable<Province>> {
        try {
            let data = await getService().get(payload.id);
            commit(provinceMutationTypes.setProvince({countryId: data?.country.id, data: data, id: payload.id}));
            return data;
        } catch (e) {
            console.error(e);
            throw e;
        }
    },

    async loadProvinces({state, commit}, {payload}) {
        let loadingState;
        if ((loadingState = state._listLoading[payload.countryId])) {
            if (loadingState.loaded) return;
            if (loadingState.promise) return;
        }

        // let promise = getService().list(payload.countryId);
        let promise = getService().list();
        commit(provinceMutationTypes.setPromise({countryId: payload.countryId, promise: promise}));

        let data: Province[] = [];
        try {
            data = await promise;
        } catch (e) {
            console.error(e);
        }
        commit(provinceMutationTypes.setProvinces({countryId: payload.countryId, data: data}));
    }
};

export const mutations: DefineMutationTree<ProvinceMutations, ProvinceState> = {
    setPromise(state, {payload}): void {
        let loadingState;
        if (!(loadingState = state._listLoading[payload.countryId])) {
            Vue.set(state._listLoading, payload.countryId, {
                loaded: false,
                promise: payload.promise
            });
        } else {
            loadingState.promise = payload.promise;
        }
    },
    setProvince(state, {payload}): void {
        let index: number = -1;
        let countryProvinces: Nullable<Province[]> = null;

        if (payload.countryId) {
            countryProvinces = state.provinces[payload.countryId];
            if (countryProvinces) {
                index = countryProvinces.map(value => value.id).indexOf(payload.id);
            } else if (payload.data) {
                countryProvinces = Vue.set(state.provinces, payload.countryId, []);
                index = -1;
            } else {
                return;
            }
        } else {
            for (let countryId of Object.keys(state.provinces)) {
                countryProvinces = state.provinces[countryId];
                index = countryProvinces.map(value => value.id).indexOf(payload.id);
                if (index >= 0) break;
            }

            if (index === -1) return;
        }

        if (index === -1) {
            if (!payload.data) return;
            if (countryProvinces) countryProvinces.push(payload.data);
            return;
        } else if (!countryProvinces) return;

        if (payload.data) Vue.set(countryProvinces, index, payload.data);
        else Vue.delete(countryProvinces, index);
    },
    setProvinces(state, {payload}): void {
        Vue.set(state.provinces, payload.countryId, payload.data);
        state._listLoading[payload.countryId].loaded = !!payload.data;
        state._listLoading[payload.countryId].promise = null;
    }
};

const store: Module<any, any> = {
    namespaced: true,
    actions,
    mutations,
    state
};

export default store;
