import {CacheableAsyncProperty, DefineTypes} from '~/store/utils/helper.types';
import {Doctor} from '~/logic/models/Doctor';
import {Patient} from '~/logic/models/Patient';
import {UserDoctors, UserPatients} from '~/logic/interfaces/services/AuthService';
import {APIError} from '~/logic/models/APIError';
import {ID, Nullable} from '~/logic/Utils';
import {User} from '~/logic/models/User';
import {APIResponse} from '~/logic/models/APIResponse';

export const LOGGED_IN_EXPIRATION_DATE_KEY = 'loggedInExpDate';
export const USER_KEY = 'user';
export const DOCTORS_KEY = 'doctors';
export const PATIENTS_KEY = 'patients';
export const IS_DOCTOR_KEY = 'isDoctor';

export interface AuthState {
    loggingIn: boolean,
    loggingOut: boolean,
    _userLoading: CacheableAsyncProperty<UserPatients | UserDoctors | APIError>,
    user: Nullable<User>,
    doctors: Doctor[],
    patients: Patient[],
    isDoctor: boolean,
    reloading: Nullable<Promise<APIResponse<UserPatients | UserDoctors>>>
}

export interface AuthActions {
    login: {
        email: string,
        password: string,
    },
    invalidate: void,
    logout: void
}

export const authActionTypes: DefineTypes<AuthActions> = {
    invalidate: payload => ({payload, type: 'invalidate'}),
    login: payload => ({payload, type: 'login'}),
    logout: payload => ({payload, type: 'logout'})
};

export interface AuthMutations {
    setUser: Nullable<User>,
    setDoctors: Nullable<Doctor[]>,
    setDoctor: {id: ID, doctor: Nullable<Doctor>},
    setPatients: Nullable<Patient[]>,
    setPromise: AuthState['_userLoading']['promise'],
    setReloading: AuthState['reloading'],
    loadUserFromLocalStorage: void
}

export const authMutationTypes: DefineTypes<AuthMutations> = {
    setPromise: payload => ({payload, type: 'setPromise'}),
    setUser: payload => ({payload, type: 'setUser'}),
    setDoctors: payload => ({payload, type: 'setDoctors'}),
    setDoctor: payload => ({payload, type: 'setDoctor'}),
    setPatients: payload => ({payload, type: 'setPatients'}),
    setReloading: payload => ({payload, type: 'setReloading'}),
    loadUserFromLocalStorage: payload => ({payload, type: 'loadUserFromLocalStorage'})
};

export interface AuthGetters {
    loggedIn: boolean
}
