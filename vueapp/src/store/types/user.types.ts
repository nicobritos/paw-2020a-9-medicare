import {DefineTypes} from '~/store/utils/helper.types';
import {CreateUserDoctor, CreateUserPatient, UpdateUser} from '~/logic/interfaces/services/UserService';
import {Nullable} from '~/logic/Utils';
import {User} from '~/logic/models/User';

export interface UserActions {
    getUser: {
        id: number
    }
    updateUser: {
        id: number,
        user: UpdateUser
    }
    createAsDoctor: {
        doctor: CreateUserDoctor
    }
    createAsPatient: {
        patient: CreateUserPatient
    }
}

export interface UserActionReturnTypes {
    getUser: Nullable<User>,
    updateUer: Nullable<User>
}

export const userActionTypes: DefineTypes<UserActions> = {
    getUser: payload => ({payload, type: 'getUser'}),
    updateUser: payload => ({payload, type: 'updateUser'}),
    createAsDoctor: payload => ({payload, type: 'createAsDoctor'}),
    createAsPatient: payload => ({payload, type: 'createAsPatient'})
};
