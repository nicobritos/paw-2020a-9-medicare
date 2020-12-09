import container from '~/plugins/inversify.config';
import TYPES from '~/logic/types';
import {DefineActionTree, DefineMutationTree} from '~/store/utils/helper.types';
import {RootState} from '~/store/types/root.types';
import {Nullable} from '~/logic/Utils';
import {Module} from 'vuex';
import {UserService} from '~/logic/interfaces/services/UserService';
import {UserActionReturnTypes, UserActions, UserState} from '~/store/types/user.types';
import {User} from '~/logic/models/User';
import {authMutationTypes} from '~/store/types/auth.types';
import {APIError} from '~/logic/models/APIError';

function getService(): UserService {
    return container.get(TYPES.Services.UserService);
}

const actions: DefineActionTree<UserActions, UserState, RootState, UserActionReturnTypes> = {
    async getUser({state, commit}, {payload}): Promise<Nullable<User>> {
        try {
            return await getService().get(payload.id);
        } catch (e) {
            console.error(e);
            return null;
        }
    },

    async updateUser({state, commit}, {payload}) {
        try {
            return await getService().update(payload.id, payload.user);
        } catch (e) {
            console.error(e);
            return null;
        }
    },

    async createAsDoctor({state, commit, dispatch}, {payload}) {
        try {
            let userDoctor = await getService().createAsDoctor(payload.doctor);
            if (userDoctor instanceof APIError) {
                console.error(userDoctor); // TODO: Guido
                return;
            }

            commit('auth/setUser', authMutationTypes.setUser(userDoctor.user));
            commit('auth/setDoctors', authMutationTypes.setDoctors(userDoctor.doctors));
        } catch (e) {
            console.error(e);
        }
    },

    async createAsPatient({state, commit}, {payload}) {
        try {
            let userPatient = await getService().createAsPatient(payload.patient);
            if (userPatient instanceof APIError) {
                console.error(userPatient); // TODO: Guido
                return;
            }

            commit('auth/setUser', authMutationTypes.setUser(userPatient.user));
            commit('auth/setPatients', authMutationTypes.setPatients(userPatient.patients));
        } catch (e) {
            console.error(e);
            return null;
        }
    }
};

const store: Module<any, any> = {
    namespaced: true,
    actions
};

export default store;
