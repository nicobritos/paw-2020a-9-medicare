import {StaffSpecialty} from '~/src/logic/models/StaffSpecialty';

export const StaffSpecialtyMIME = {
    LIST: 'application/vnd.specialty.list.get.v1+json',
};

export interface StaffService {
    list(): Promise<StaffSpecialty[]>;
}
