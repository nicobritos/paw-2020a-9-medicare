import {APIError} from '~/logic/models/APIError';
import {User} from '~/logic/models/User';
import {Base64, Nullable} from '~/logic/Utils';
import {UserDoctors, UserPatients} from '~/logic/interfaces/services/AuthService';

interface CreateUser {
    email: string;
    password: string;
    firstName: string;
    surname: string;
    phone?: string;
}

export interface CreateUserDoctor extends CreateUser {
    registrationNumber?: number;
    specialtyIds: number[]; // TODO: APIDocs dice que no es mandatory
    localityId: number;
}

export interface CreateUserPatient extends CreateUser {
}

export interface UpdateUser {
    email?: string;
    firstName?: string;
    surname?: string;
    phone?: string;
}

export interface UserService {
    createAsDoctor(doctor: CreateUserDoctor): Promise<UserDoctors | APIError>;

    createAsPatient(patient: CreateUserPatient): Promise<UserPatients | APIError>;

    get(id: number): Promise<Nullable<User>>;

    me(): Promise<UserDoctors | UserPatients | APIError>;

    update(id: number, user: UpdateUser): Promise<User | APIError>
}
