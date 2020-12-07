import {LocalityState} from '~/store/types/localities.types';
import {UserState} from '~/store/types/user.types';
import {DoctorSpecialtyState} from '~/store/types/doctorSpecialties.types';
import {AuthState} from '~/store/types/auth.types';

export interface RootState {
    auth: AuthState,
    localities: LocalityState,
    users: UserState,
    doctorSpecialties: DoctorSpecialtyState
}
