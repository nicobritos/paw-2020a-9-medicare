import {APIError} from '~/logic/models/APIError';
import {User} from '~/logic/models/User';
import {Nullable} from '~/logic/Utils';
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
}

export interface CreateUserPatient extends CreateUser {
}

// TODO: Ver los required en api doc
export interface UpdateUser {
    email?: string;
    firstName?: string;
    surname?: string;
    phone?: string;
    profilePictureId?: number; // TODO --> tendria q ser un multipart
}

export interface UserService {
    // TODO: API Doc ver 404
    // TODO: API
    createAsDoctor(doctor: CreateUserDoctor): Promise<UserDoctors | APIError>;

    createAsPatient(patient: CreateUserPatient): Promise<UserPatients | APIError>;

    get(id: number): Promise<Nullable<User>>;

    update(id: number, user: UpdateUser): Promise<User | APIError>
}
