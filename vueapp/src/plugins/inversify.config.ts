import {Container} from 'inversify';
import 'reflect-metadata';
import {RestRepository} from '~/src/logic/interfaces/repositories/RestRepository';
import TYPES from '~/src/logic/types';
import {RestRepositoryImpl} from '~/src/logic/repositories/RestRepositoryImpl';
import {CountryService} from '~/src/logic/interfaces/services/CountryService';
import {CountryServiceImpl} from '~/src/logic/services/CountryServiceImpl';

const container = new Container();

container.bind<RestRepository>(TYPES.Repositories.RestRepository).to(RestRepositoryImpl).inSingletonScope();

container.bind<CountryService>(TYPES.Services.CountryService).to(CountryServiceImpl).inSingletonScope();

export default container;
