import {Nullable} from '~/logic/models/utils/Utils';
import {inject, injectable} from 'inversify';
import TYPES from '~/logic/types';
import {RestRepository} from '~/logic/interfaces/repositories/RestRepository';
import {getPathWithId} from '~/logic/services/Utils';
import {APIError} from '~/logic/models/APIError';
import {StaffService, UpdateStaff} from '~/logic/interfaces/services/StaffService';
import {Staff} from '~/logic/models/Staff';
import {Pagination} from '~/logic/models/utils/Pagination';

const StaffMIME = {
    LIST: 'application/vnd.staff.list.get.v1+json',
    GET: 'application/vnd.staff.get.v1+json',
    UPDATE: 'application/vnd.staff.update.v1+json'
};

@injectable()
export class StaffServiceImpl implements StaffService {
    private static PATH = 'staffs';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    public async get(id: number): Promise<Nullable<Staff>> {
        let response = await this.rest.get<Staff>(getPathWithId(StaffServiceImpl.PATH, id), {
            accepts: StaffMIME.GET
        });
        return response.isOk() ? response.data! : null;
    }

    public async list(): Promise<Pagination<Staff>> {
        let response = await this.rest.get<Pagination<Staff>>(StaffServiceImpl.PATH, {
            accepts: StaffMIME.LIST,
            paginate: true
        });
        return response.isOk() ? response.data! : new Pagination([], 0);
    }

    public async update(id: number, staff: UpdateStaff): Promise<Staff | APIError> {
        let response = await this.rest.post<Staff, UpdateStaff>(StaffServiceImpl.PATH, {
            accepts: StaffMIME.GET,
            data: staff,
            contentType: StaffMIME.UPDATE
        });
        return response.isOk() ? response.data! : response.error!;
    }
}
