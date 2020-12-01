import {StaffSpecialty} from '~/src/logic/models/StaffSpecialty';

export interface StaffSpecialtyService {
    list(): Promise<StaffSpecialty[]>;
}
