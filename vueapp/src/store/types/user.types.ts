import {CacheableAsyncProperty, DefineTypes} from '~/store/utils/helper.types';
import {Nullable} from '~/logic/Utils';
import {User} from '~/logic/models/User';

export interface UserState {
    user: Nullable<User>
    _loadingPromise: CacheableAsyncProperty<User>
}

export interface UserActions {
    getUser: {}
}

export const userActionTypes: DefineTypes<UserActions> = {
    getUser: payload => ({payload, type: 'getUser'})
};

export interface UserMutations {
    setUser: UserState['user'],
    setPromise: UserState['_loadingPromise']['promise'],
}

export const userMutationTypes: DefineTypes<UserMutations> = {
    setPromise: payload => ({payload, type: 'setPromise'}),
    setUser: payload => ({payload, type: 'setUser'}),
};
