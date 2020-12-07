import {AuthService} from '~/logic/interfaces/services/AuthService';
import {User} from '~/logic/models/User';
import TYPES from '~/logic/types';
import container from '~/plugins/inversify.config';
import {AuthActions, AuthGetters, AuthMutations, authMutationTypes, AuthState} from '~/store/types/auth.types';
import {RootState} from '~/store/types/root.types';
import {DefineActionTree, DefineGetterTree, DefineMutationTree} from '~/store/utils/helper.types';
import {Nullable} from '~/logic/models/utils/Utils';
import {Module} from 'vuex';

function getService(): AuthService {
    return container.get(TYPES.Services.AuthService);
}

const state = (): AuthState => ({
    _userLoading: {
        promise: null
    },
    loggingIn: false,
    loggingOut: false,
    user: null as Nullable<User>
});

const getters: DefineGetterTree<AuthGetters, AuthState, RootState> = {
    loggedIn(state): boolean {
        return !!state.user;
    }
};

const actions: DefineActionTree<AuthActions, AuthState, RootState> = {
    async invalidate({state, commit}) {
        if (state._userLoading.promise)
            await state._userLoading.promise;

        commit(authMutationTypes.setPromise(null));
        commit(authMutationTypes.setUser(null));
    },
    async login({state, commit}, {payload}) {
        if (state._userLoading.promise) return;

        let promise = getService().login({
            username: payload.email,
            password: payload.password
        });
        commit(authMutationTypes.setPromise(promise));

        let data = null;
        try {
            data = await promise;
        } catch (e) {
            console.error(e);
        }
        commit(authMutationTypes.setUser(data));
    },
    async logout({state, commit}) {
        if (!state._userLoading.promise && !state.user) return;

        if (state._userLoading.promise)
            await state._userLoading.promise;

        commit(authMutationTypes.setPromise(null));
        try {
            await getService().logout();
        } catch (e) {
            console.error(e);
        }
        commit(authMutationTypes.setUser(null));
    },
    async refresh({state, commit}) {
        if (state.user) return;

        let promise = getService().refresh();
        commit(authMutationTypes.setPromise(promise));

        let data = null;
        try {
            data = await promise;
        } catch (e) {
            console.error(e);
        }
        commit(authMutationTypes.setUser(data));
    }
};

const mutations: DefineMutationTree<AuthMutations, AuthState> = {
    setPromise(state, {payload}): void {
        if (payload) state.loggingIn = true;
        else state.loggingOut = true;
        state._userLoading.promise = payload;
    },
    setUser(state, {payload}): void {
        state.loggingOut = state.loggingIn = false;

        state.user = payload;
        state._userLoading.promise = null;
    }
};

const store: Module<any, any> = {
    namespaced: true,
    actions,
    mutations,
    getters,
    state
};

export default store;
