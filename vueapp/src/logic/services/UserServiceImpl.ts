import {Nullable} from '~/src/logic/models/utils/Utils';
import {inject, injectable} from 'inversify';
import TYPES from '~/src/logic/types';
import {RestRepository} from '~/src/logic/interfaces/repositories/RestRepository';
import {getPathWithId} from '~/src/logic/services/Utils';
import {APIError} from '~/src/logic/models/APIError';
import {CreateUserPatient, CreateUserStaff, UpdateUser, UserService} from '~/src/logic/interfaces/services/UserService';
import {User} from '~/src/logic/models/User';

const UserMIME = {
    CREATE_PATIENT: 'application/vnd.user.patient.create.v1+json',
    CREATE_STAFF: 'application/vnd.user.staff.create.v1+json',
    GET: 'application/vnd.user.get.v1+json',
    UPDATE: 'application/vnd.user.update.v1+json'
};

@injectable()
export class UserServiceImpl implements UserService {
    private static PATH = 'users';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    public async get(id: number): Promise<Nullable<User>> {
        let response = await this.rest.get<User>(getPathWithId(UserServiceImpl.PATH, id), {
            accepts: UserMIME.GET
        });
        return response.isOk() ? response.data! : null;
    }

    public async createAsStaff(staff: CreateUserStaff): Promise<User | APIError> {
        let response = await this.rest.post<User, CreateUserStaff>(UserServiceImpl.PATH, {
            accepts: UserMIME.GET,
            data: staff,
            contentType: UserMIME.CREATE_STAFF
        });
        return response.isOk() ? response.data! : response.error!;
    }

    public async createAsPatient(patient: CreateUserPatient): Promise<User | APIError> {
        let response = await this.rest.post<User, CreateUserPatient>(UserServiceImpl.PATH, {
            accepts: UserMIME.GET,
            data: patient,
            contentType: UserMIME.CREATE_PATIENT
        });
        return response.isOk() ? response.data! : response.error!;
    }

    public async update(id: number, user: UpdateUser): Promise<User | APIError> {
        let response = await this.rest.post<User, UpdateUser>(getPathWithId(UserServiceImpl.PATH, id), {
            accepts: UserMIME.GET,
            data: user,
            contentType: UserMIME.UPDATE
        });
        return response.isOk() ? response.data! : response.error!;
    }
}
