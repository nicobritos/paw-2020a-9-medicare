import {Staff} from '~/src/logic/models/Staff';
import {StaffSpecialty} from '~/src/logic/models/StaffSpecialty';
import {APIError} from '~/src/logic/models/APIError';
import {Pagination} from '~/src/logic/models/utils/Pagination';
import {Nullable} from '~/src/logic/models/utils/Utils';

export const StaffMIME = {
    LIST: 'application/vnd.staff.list.get.v1+json',
    GET: 'application/vnd.staff.get.v1+json',
    UPDATE: 'application/vnd.staff.update.v1+json'
};

// TODO: Ver los required en api doc
export interface UpdateStaff {
    phone?: string;
    email?: string;
    specialtyIds?: StaffSpecialty[]; // TODO: No tendrian q ser ids?
}

export interface StaffService {
    list(): Promise<Pagination<Staff>>;

    get(id: number): Promise<Nullable<Staff>>;

    update(id: number, staff: Staff): Promise<Staff | APIError>
}
