import {Staff} from '~/logic/models/Staff';
import {StaffSpecialty} from '~/logic/models/StaffSpecialty';
import {APIError} from '~/logic/models/APIError';
import {Pagination} from '~/logic/models/utils/Pagination';
import {Nullable} from '~/logic/models/utils/Utils';

// TODO: Ver los required en api doc
export interface UpdateStaff {
    phone?: string;
    email?: string;
    specialtyIds?: StaffSpecialty[]; // TODO: No tendrian q ser ids?
}

export interface StaffService {
    list(): Promise<Pagination<Staff>>;

    get(id: number): Promise<Nullable<Staff>>;

    update(id: number, staff: UpdateStaff): Promise<Staff | APIError>
}
