import {Nullable} from '~/logic/models/utils/Utils';
import {inject, injectable} from 'inversify';
import TYPES from '~/logic/types';
import {RestRepository} from '~/logic/interfaces/repositories/RestRepository';
import {getPathWithId} from '~/logic/services/Utils';
import {APIError} from '~/logic/models/APIError';
import {CreateUserPatient, CreateUserDoctor, UpdateUser, UserService} from '~/logic/interfaces/services/UserService';
import {User} from '~/logic/models/User';

export const UserMIME = {
    CREATE_PATIENT: 'application/vnd.user.patient.create.v1+json',
    CREATE_DOCTOR: 'application/vnd.user.doctor.create.v1+json',
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

    public async createAsDoctor(doctor: CreateUserDoctor): Promise<User | APIError> {
        let response = await this.rest.post<User, CreateUserDoctor>(UserServiceImpl.PATH, {
            accepts: UserMIME.GET,
            data: doctor,
            contentType: UserMIME.CREATE_DOCTOR
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
