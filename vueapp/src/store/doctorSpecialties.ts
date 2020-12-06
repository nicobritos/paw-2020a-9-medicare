import container from '~/plugins/inversify.config';
import TYPES from '~/logic/types';
import {DefineActionTree, DefineMutationTree} from '~/store/utils/helper.types';
import {RootState} from '~/store/types/root.types';
import {Module} from 'vuex';
import {
    StaffSpecialtyActions, StaffSpecialtyMutations,
    staffSpecialtyMutationTypes,
    DoctorSpecialtyState,
} from '~/store/types/doctorSpecialties.types';
import {DoctorSpecialtyService} from '~/logic/interfaces/services/DoctorSpecialtyService';
import {DoctorSpecialty} from '~/logic/models/DoctorSpecialty';

function getService(): DoctorSpecialtyService {
    return container.get(TYPES.Services.DoctorSpecialtyService);
}

export const state = (): DoctorSpecialtyState => ({
    _listLoading: {
        loaded: false,
        promise: null
    } as DoctorSpecialtyState['_listLoading'],
    doctorSpecialties: [] as DoctorSpecialty[]
});

export const actions: DefineActionTree<StaffSpecialtyActions, DoctorSpecialtyState, RootState> = {
    async loadStaffSpecialties({state, commit}) {
        if (state._listLoading.loaded) return;
        if (state._listLoading.promise) return;

        let promise = getService().list();
        commit(staffSpecialtyMutationTypes.setPromise(promise));

        let data: DoctorSpecialty[] = [];
        try {
            data = await promise;
        } catch (e) {
            console.error(e);
        }

        commit(staffSpecialtyMutationTypes.setStaffSpecialties(data));
    }
};

export const mutations: DefineMutationTree<StaffSpecialtyMutations, DoctorSpecialtyState> = {
    setPromise(state, {payload}): void {
        state._listLoading.promise = payload;
    },
    setStaffSpecialties(state, {payload}): void {
        state.doctorSpecialties = payload;
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
