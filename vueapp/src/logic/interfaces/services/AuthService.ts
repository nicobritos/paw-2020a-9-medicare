import {User} from '~/logic/models/User';
import {Nullable} from '~/logic/models/utils/Utils';

export interface LoginUser {
    username: string;
    password: string;
}

export interface AuthService {
    login(loginUser: LoginUser): Promise<Nullable<User>>;

    logout(): Promise<void>;

    refresh(): Promise<Nullable<User>>;
}
