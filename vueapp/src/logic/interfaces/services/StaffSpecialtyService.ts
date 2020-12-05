import {StaffSpecialty} from '~/logic/models/StaffSpecialty';

export interface StaffSpecialtyService {
    list(): Promise<StaffSpecialty[]>;
}
