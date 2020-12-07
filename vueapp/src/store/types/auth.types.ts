import {User} from '~/logic/models/User';
import {CacheableAsyncProperty, DefineTypes} from '~/store/utils/helper.types';
import {Nullable} from '~/logic/models/utils/Utils';

export interface AuthState {
    loggingIn: boolean,
    loggingOut: boolean,
    user: Nullable<User>,
    _userLoading: CacheableAsyncProperty<Nullable<User>>
}

export interface AuthActions {
    login: {
        email: string,
        password: string,
    },
    invalidate: void,
    logout: void,
    refresh: void
}

export const authActionTypes: DefineTypes<AuthActions> = {
    invalidate: payload => ({payload, type: 'invalidate'}),
    login: payload => ({payload, type: 'login'}),
    logout: payload => ({payload, type: 'logout'}),
    refresh: payload => ({payload, type: 'refresh'})
};

export interface AuthMutations {
    setUser: AuthState['user'],
    setPromise: AuthState['_userLoading']['promise']
}

export const authMutationTypes: DefineTypes<AuthMutations> = {
    setPromise: payload => ({payload, type: 'setPromise'}),
    setUser: payload => ({payload, type: 'setUser'})
};

export interface AuthGetters {
    loggedIn: boolean
}
