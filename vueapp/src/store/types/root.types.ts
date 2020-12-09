import {LocalityState} from '~/store/types/localities.types';
import {DoctorSpecialtyState} from '~/store/types/doctorSpecialties.types';
import {AuthState} from '~/store/types/auth.types';

export interface RootState {
    auth: AuthState,
    localities: LocalityState,
    doctorSpecialties: DoctorSpecialtyState
}
