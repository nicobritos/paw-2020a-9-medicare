import {Nullable} from '~/logic/models/utils/Utils';
import {inject, injectable} from 'inversify';
import TYPES from '~/logic/types';
import {RestRepository} from '~/logic/interfaces/repositories/RestRepository';
import {getPathWithId} from '~/logic/services/Utils';
import {ProvinceService} from '~/logic/interfaces/services/ProvinceService';
import {Province} from '~/logic/models/Province';

const ProvinceMIME = {
    LIST: 'application/vnd.province.list.get.v1+json',
    GET: 'application/vnd.province.get.v1+json',
};

@injectable()
export class ProvinceServiceImpl implements ProvinceService {
    private static PATH = 'provinces';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    // TODO: Manage errors
    public async list(): Promise<Province[]> {
        let response = await this.rest.get<Province[]>(ProvinceServiceImpl.PATH, {
            accepts: ProvinceMIME.LIST
        });
        return response.isOk() ? response.data! : [];
    }

    public async get(id: number): Promise<Nullable<Province>> {
        let response = await this.rest.get<Province>(getPathWithId(ProvinceServiceImpl.PATH, id), {
            accepts: ProvinceMIME.GET
        });
        return response.isOk() ? response.data! : null;
    }
}
