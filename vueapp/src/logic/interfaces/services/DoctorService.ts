import {Doctor} from '~/logic/models/Doctor';
import {DoctorSpecialty} from '~/logic/models/DoctorSpecialty';
import {APIError} from '~/logic/models/APIError';
import {Pagination} from '~/logic/models/utils/Pagination';
import {Nullable} from '~/logic/Utils';

// TODO: Ver los required en api doc
export interface UpdateDoctor {
    phone?: string;
    email?: string;
    specialtyIds?: DoctorSpecialty[]; // TODO: No tendrian q ser ids?
}

export interface DoctorService {
    list(): Promise<Pagination<Doctor>>;

    get(id: number): Promise<Nullable<Doctor>>;

    update(id: number, doctor: UpdateDoctor): Promise<Doctor | APIError>
}
