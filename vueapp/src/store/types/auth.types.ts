import {User} from '~/logic/models/User';
import {CacheableAsyncProperty, DefineTypes} from '~/store/utils/helper.types';
import {Nullable} from '~/logic/Utils';
import {Doctor} from '~/logic/models/Doctor';
import {Patient} from '~/logic/models/Patient';
import {UserDoctors, UserPatients} from '~/logic/interfaces/services/AuthService';

export interface AuthState {
    loggingIn: boolean,
    loggingOut: boolean,
    user: Nullable<User>,
    _userLoading: CacheableAsyncProperty<Nullable<UserPatients | UserDoctors>>,
    doctors: Doctor[],
    patients: Patient[],
    isDoctor: boolean
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
    setUser: AuthState['user'],
    setDoctors: AuthState['doctors'],
    setPatients: AuthState['patients'],
    setPromise: AuthState['_userLoading']['promise']
}

export const authMutationTypes: DefineTypes<AuthMutations> = {
    setPromise: payload => ({payload, type: 'setPromise'}),
    setUser: payload => ({payload, type: 'setUser'}),
    setDoctors: payload => ({payload, type: 'setDoctors'}),
    setPatients: payload => ({payload, type: 'setPatients'})
};

export interface AuthGetters {
    loggedIn: boolean
}
