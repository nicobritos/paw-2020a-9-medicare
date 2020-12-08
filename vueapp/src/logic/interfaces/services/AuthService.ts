import {User} from '~/logic/models/User';
import {Nullable} from '~/logic/Utils';
import {Patient} from '~/logic/models/Patient';
import {Doctor} from '~/logic/models/Doctor';

export interface LoginUser {
    username: string;
    password: string;
}

export interface UserPatients {
    user: User
    patients: Patient[]
}

export interface UserDoctors {
    user: User
    doctors: Doctor[]
}

export interface AuthService {
    login(loginUser: LoginUser): Promise<Nullable<UserPatients | UserDoctors>>;

    logout(): Promise<void>;

    refresh(): Promise<Nullable<UserPatients | UserDoctors>>;
}
