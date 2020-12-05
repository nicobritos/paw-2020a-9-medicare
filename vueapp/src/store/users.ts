import container from '~/plugins/inversify.config';
import TYPES from '~/logic/types';
import {DefineActionTree, DefineMutationTree} from '~/store/utils/helper.types';
import {RootState} from '~/store/types/root.types';
import {Nullable} from '~/logic/models/utils/Utils';
import {Module} from 'vuex';
import {UserService} from '~/logic/interfaces/services/UserService';
import {UserActions, UserMutations, UserState} from '~/store/types/user.types';
import {User} from '~/logic/models/User';

function getService(): UserService {
    return container.get(TYPES.Services.UserService);
}

export const state = (): UserState => ({
    _loadingPromise: {
        promise: null,
        loaded: false
    } as UserState['_loadingPromise'],
    user: null as Nullable<User>
});

export const actions: DefineActionTree<UserActions, UserState, RootState> = {
    async getUser({state, commit}, {payload}) {
        // try {
            // let data = await getService().get(me);
            // commit(provinceMutationTypes.setProvince({countryId: data?.country.id, data: data, id: payload.id}));
            // return data;
        // } catch (e) {
        //     console.error(e);
        //     throw e;
        // }
    }
};

export const mutations: DefineMutationTree<UserMutations, UserState> = {
    setPromise(state, {payload}): void {
        state._loadingPromise.promise = payload;
    },
    setUser(state, {payload}): void {
        state.user = payload;
        state._loadingPromise.loaded = true;
        state._loadingPromise.promise = null;
    }
};

const store: Module<any, any> = {
    namespaced: true,
    actions,
    mutations,
    state
};

export default store;
