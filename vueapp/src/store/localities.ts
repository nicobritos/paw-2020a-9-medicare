import container from '~/plugins/inversify.config';
import TYPES from '~/logic/types';
import {DefineActionTree, DefineMutationTree} from '~/store/utils/helper.types';
import {RootState} from '~/store/types/root.types';
import {Module} from 'vuex';
import {LocalityService} from '~/logic/interfaces/services/LocalityService';
import {LocalityActions, LocalityMutations, localityMutationTypes, LocalityState} from '~/store/types/localities.types';
import {Locality} from '~/logic/models/Locality';

function getService(): LocalityService {
    return container.get(TYPES.Services.LocalityService);
}

const state = (): LocalityState => ({
    _listLoading: {
        loaded: false,
        promise: null
    } as LocalityState['_listLoading'],
    localities: [] as Locality[]
});

const actions: DefineActionTree<LocalityActions, LocalityState, RootState> = {
    async loadLocalities({state, commit}) {
        if (state._listLoading.loaded) return;
        if (state._listLoading.promise) return;

        let promise = getService().list();
        commit(localityMutationTypes.setPromise(promise));

        let data: Locality[] = [];
        try {
            data = await promise;
        } catch (e) {
            console.error(e);
        }

        commit(localityMutationTypes.setLocalities(data));
    }
};

const mutations: DefineMutationTree<LocalityMutations, LocalityState> = {
    setPromise(state, {payload}): void {
        state._listLoading.promise = payload;
    },
    setLocalities(state, {payload}): void {
        state.localities = payload;
        state._listLoading.loaded = true;
        state._listLoading.promise = null;
    }
};

const store: Module<any, any> = {
    namespaced: true,
    actions,
    mutations,
    state
};

export default store;
