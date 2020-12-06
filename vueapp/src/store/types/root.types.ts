import {LocalityState} from '~/store/types/localities.types';
import {UserState} from '~/store/types/user.types';
import {DoctorSpecialtyState} from '~/store/types/doctorSpecialties.types';

export interface RootState {
    localities: LocalityState,
    users: UserState,
    staffSpecialties: DoctorSpecialtyState
}
