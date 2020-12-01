import {Container} from 'inversify';
import 'reflect-metadata';
import {RestRepository} from '~/src/logic/interfaces/repositories/RestRepository';
import TYPES from '~/src/logic/types';
import {RestRepositoryImpl} from '~/src/logic/repositories/RestRepositoryImpl';
import {CountryService} from '~/src/logic/interfaces/services/CountryService';
import {CountryServiceImpl} from '~/src/logic/services/CountryServiceImpl';
import {AppointmentService} from '~/src/logic/interfaces/services/AppointmentService';
import {AppointmentServiceImpl} from '~/src/logic/services/AppointmentServiceImpl';
import {AppointmentTimeSlotServiceImpl} from '~/src/logic/services/AppointmentTimeSlotServiceImpl';
import {LocalityServiceImpl} from '~/src/logic/services/LocalityServiceImpl';
import {LocalityService} from '~/src/logic/interfaces/services/LocalityService';
import {AppointmentTimeSlotService} from '~/src/logic/interfaces/services/AppointmentTimeSlotService';
import {ProvinceService} from '~/src/logic/interfaces/services/ProvinceService';
import {ProvinceServiceImpl} from '~/src/logic/services/ProvinceServiceImpl';
import {StaffServiceImpl} from '~/src/logic/services/StaffServiceImpl';
import {StaffService} from '~/src/logic/interfaces/services/StaffService';
import {StaffSpecialtyService} from '~/src/logic/interfaces/services/StaffSpecialtyService';
import {UserService} from '~/src/logic/interfaces/services/UserService';
import {WorkdayService} from '~/src/logic/interfaces/services/WorkdayService';
import {WorkdayServiceImpl} from '~/src/logic/services/WorkdayServiceImpl';
import {UserServiceImpl} from '~/src/logic/services/UserServiceImpl';
import {StaffSpecialtyServiceImpl} from '~/src/logic/services/StaffSpecialtyServiceImpl';

const container = new Container();

container.bind<RestRepository>(TYPES.Repositories.RestRepository).to(RestRepositoryImpl).inSingletonScope();

container.bind<AppointmentService>(TYPES.Services.AppointmentService).to(AppointmentServiceImpl).inSingletonScope();
container.bind<AppointmentTimeSlotService>(TYPES.Services.AppointmentTimeSlotService).to(AppointmentTimeSlotServiceImpl).inSingletonScope();
container.bind<CountryService>(TYPES.Services.CountryService).to(CountryServiceImpl).inSingletonScope();
container.bind<LocalityService>(TYPES.Services.LocalityService).to(LocalityServiceImpl).inSingletonScope();
container.bind<ProvinceService>(TYPES.Services.ProvinceService).to(ProvinceServiceImpl).inSingletonScope();
container.bind<StaffService>(TYPES.Services.StaffService).to(StaffServiceImpl).inSingletonScope();
container.bind<StaffSpecialtyService>(TYPES.Services.StaffSpecialtyService).to(StaffSpecialtyServiceImpl).inSingletonScope();
container.bind<UserService>(TYPES.Services.UserService).to(UserServiceImpl).inSingletonScope();
container.bind<WorkdayService>(TYPES.Services.WorkdayService).to(WorkdayServiceImpl).inSingletonScope();

export default container;
