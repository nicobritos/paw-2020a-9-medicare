import {Nullable} from '~/src/logic/models/utils/Utils';
import {inject, injectable} from 'inversify';
import TYPES from '~/src/logic/types';
import {RestRepository} from '~/src/logic/interfaces/repositories/RestRepository';
import {getPathWithId} from '~/src/logic/services/Utils';
import {LocalityService} from '~/src/logic/interfaces/services/LocalityService';
import {Locality} from '~/src/logic/models/Locality';

const LocalityMIME = {
    LIST: 'application/vnd.locality.list.get.v1+json',
    GET: 'application/vnd.locality.get.v1+json',
};

@injectable()
export class LocalityServiceImpl implements LocalityService {
    private static PATH = 'localities';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    // TODO: Manage errors
    public async list(): Promise<Locality[]> {
        let response = await this.rest.get<Locality[]>(LocalityServiceImpl.PATH, {
            accepts: LocalityMIME.LIST
        });
        return response.isOk() ? response.data! : [];
    }

    public async get(id: number): Promise<Nullable<Locality>> {
        let response = await this.rest.get<Locality>(getPathWithId(LocalityServiceImpl.PATH, id), {
            accepts: LocalityMIME.GET
        });
        return response.isOk() ? response.data! : null;
    }
}
