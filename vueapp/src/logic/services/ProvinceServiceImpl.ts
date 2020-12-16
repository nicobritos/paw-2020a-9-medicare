import {Nullable} from '~/logic/Utils';
import {inject, injectable} from 'inversify';
import TYPES from '~/logic/types';
import {GetConfig, RestRepository} from '~/logic/interfaces/repositories/RestRepository';
import {getPathWithId} from '~/logic/services/Utils';
import {ProvinceService} from '~/logic/interfaces/services/ProvinceService';
import {Province} from '~/logic/models/Province';
import {APIError} from '~/logic/models/APIError';

const ProvinceMIME = {
    LIST: 'application/vnd.province.list.get.v1+json',
    GET: 'application/vnd.province.get.v1+json',
};

@injectable()
export class ProvinceServiceImpl implements ProvinceService {
    private static PATH = 'provinces';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    public async list(countryId?: string): Promise<Nullable<Province[]> | APIError> {
        let config: GetConfig<any> = {
            accepts: ProvinceMIME.LIST
        };
        if (countryId) {
            config.params = {
                countryId
            };
        }

        let response = await this.rest.get<Province[]>(ProvinceServiceImpl.PATH, config);
        return response.nullableResponse;
    }

    public async get(id: number): Promise<Nullable<Province>> {
        let response = await this.rest.get<Province>(getPathWithId(ProvinceServiceImpl.PATH, id), {
            accepts: ProvinceMIME.GET
        });
        return response.orElse(null);
    }
}
