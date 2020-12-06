import {CacheableAsyncProperty, DefineTypes} from '~/store/utils/helper.types';
import {DoctorSpecialty} from '~/logic/models/DoctorSpecialty';

export interface DoctorSpecialtyState {
    doctorSpecialties: DoctorSpecialty[],
    _listLoading: CacheableAsyncProperty<DoctorSpecialty[]>
}

export interface StaffSpecialtyActions {
    loadStaffSpecialties: void
}

export const staffSpecialtyActionTypes: DefineTypes<StaffSpecialtyActions> = {
    loadStaffSpecialties: payload => ({payload, type: 'loadStaffSpecialties'})
};

export interface StaffSpecialtyMutations {
    setPromise: Promise<DoctorSpecialty[]>,
    setStaffSpecialties: DoctorSpecialty[]
}

export const staffSpecialtyMutationTypes: DefineTypes<StaffSpecialtyMutations> = {
    setPromise: payload => ({payload, type: 'setPromise'}),
    setStaffSpecialties: payload => ({payload, type: 'setStaffSpecialties'})
};
