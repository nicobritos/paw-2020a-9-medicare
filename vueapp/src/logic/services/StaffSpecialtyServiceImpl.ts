import {inject, injectable} from 'inversify';
import TYPES from '~/src/logic/types';
import {RestRepository} from '~/src/logic/interfaces/repositories/RestRepository';
import {StaffSpecialtyService} from '~/src/logic/interfaces/services/StaffSpecialtyService';
import {StaffSpecialty} from '~/src/logic/models/StaffSpecialty';

const StaffSpecialtyMIME = {
    LIST: 'application/vnd.specialty.list.get.v1+json',
};

@injectable()
export class StaffSpecialtyServiceImpl implements StaffSpecialtyService {
    private static PATH = 'specialties';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    // TODO: Manage errors
    public async list(): Promise<StaffSpecialty[]> {
        let response = await this.rest.get<StaffSpecialty[]>(StaffSpecialtyServiceImpl.PATH, {
            accepts: StaffSpecialtyMIME.LIST
        });
        return response.isOk() ? response.data! : [];
    }
}