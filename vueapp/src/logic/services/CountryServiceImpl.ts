import {Country} from '~/src/logic/models/Country';
import {Nullable} from '~/src/logic/models/utils/Utils';
import {CountryService} from '~/src/logic/interfaces/services/CountryService';
import {inject, injectable} from 'inversify';
import TYPES from '~/src/logic/types';
import {RestRepository} from '~/src/logic/interfaces/repositories/RestRepository';
import {getPathWithId} from '~/src/logic/services/Utils';

const CountryServiceMIME = {
    LIST: 'application/vnd.country.list.get.v1+json',
    GET: 'application/vnd.country.get.v1+json',
};

@injectable()
export class CountryServiceImpl implements CountryService {
    private static PATH = 'countries';

    @inject(TYPES.Repositories.RestRepository)
    private rest: RestRepository;

    // TODO: Manage errors
    public async list(): Promise<Country[]> {
        let response = await this.rest.get<Country[]>(CountryServiceImpl.PATH, {
            accepts: CountryServiceMIME.LIST
        });
        return response.isOk() ? response.data! : [];
    }

    public async get(id: string): Promise<Nullable<Country>> {
        let response = await this.rest.get<Country>(getPathWithId(CountryServiceImpl.PATH, id), {
            accepts: CountryServiceMIME.GET
        });
        return response.isOk() ? response.data! : null;
    }
}
