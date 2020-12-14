import {Nullable} from '~/logic/Utils';
import {inject, injectable} from 'inversify';
import TYPES from '~/logic/types';
import {RestRepository} from '~/logic/interfaces/repositories/RestRepository';
import {getPathWithId} from '~/logic/services/Utils';
import {APIError} from '~/logic/models/APIError';
import {DoctorSearchParams, DoctorService, UpdateDoctor} from '~/logic/interfaces/services/DoctorService';
import {Doctor} from '~/logic/models/Doctor';
import {Pagination} from '~/logic/models/utils/Pagination';

const DoctorMIME = {
    LIST: 'application/vnd.doctor.list.get.v1+json',
    GET: 'application/vnd.doctor.get.v1+json',
    UPDATE: 'application/vnd.doctor.update.v1+json',
    PAGINATION: 'application/vnd.doctor.pagination.v1+json',
};

@injectable()
export class DoctorServiceImpl implements DoctorService {
    private static PATH = 'doctors';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    public async get(id: number): Promise<Nullable<Doctor>> {
        let response = await this.rest.get<Doctor>(getPathWithId(DoctorServiceImpl.PATH, id), {
            accepts: DoctorMIME.GET
        });
        return response.isOk() ? response.data! : null;
    }

    public async list(params: DoctorSearchParams): Promise<Pagination<Doctor>> {
        let response = await this.rest.get<Pagination<Doctor>>(DoctorServiceImpl.PATH, {
            accepts: DoctorMIME.LIST,
            paginate: true,
            data: params,
            contentType: DoctorMIME.PAGINATION
        });
        return response.isOk() ? response.data! : new Pagination([], 0);
    }

    public async update(id: number, doctor: UpdateDoctor): Promise<Doctor | APIError> {
        let response = await this.rest.post<Doctor, UpdateDoctor>(DoctorServiceImpl.PATH, {
            accepts: DoctorMIME.GET,
            data: doctor,
            contentType: DoctorMIME.UPDATE
        });
        return response.isOk() ? response.data! : response.error!;
    }
}
