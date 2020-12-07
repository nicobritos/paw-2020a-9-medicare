import {inject, injectable} from 'inversify';
import TYPES from '~/logic/types';
import {RestRepository} from '~/logic/interfaces/repositories/RestRepository';
import {Nullable} from '~/logic/models/utils/Utils';
import {AuthService, LoginUser, UserDoctors, UserPatients} from '~/logic/interfaces/services/AuthService';
import {User} from '~/logic/models/User';
import {ErrorMIME} from '~/logic/models/APIError';
import {JSON_MIME} from '~/logic/services/Utils';
import {UserMIME} from '~/logic/services/UserServiceImpl';

const AuthMIME = {
    LOGIN: 'application/vnd.login.post.v1+json'
};

@injectable()
export class AuthServiceImpl implements AuthService {
    private static LOGIN_PATH = 'login';
    private static REFRESH_PATH = 'auth/refresh';
    private static LOGOUT_PATH = 'auth/logout';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    public async login(loginUser: LoginUser): Promise<Nullable<UserPatients | UserDoctors>> {
        let response = await this.rest.post<UserPatients | UserDoctors, LoginUser>(AuthServiceImpl.LOGIN_PATH, {
            accepts: UserMIME.GET,
            data: loginUser,
            contentType: AuthMIME.LOGIN
        });

        return response.isOk() ? response.data! : null;
    }

    public async refresh(): Promise<Nullable<UserPatients | UserDoctors>> {
        let response = await this.rest.post<UserPatients | UserDoctors, void>(AuthServiceImpl.REFRESH_PATH, {
            accepts: UserMIME.GET,
            data: undefined,
            contentType: JSON_MIME
        });

        return response.isOk() ? response.data! : null;
    }

    public async logout(): Promise<void> {
        await this.rest.post<User, void>(AuthServiceImpl.LOGOUT_PATH, {
            accepts: ErrorMIME,
            data: undefined,
            contentType: JSON_MIME
        });
    }
}
