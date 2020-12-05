import {GenericEntity} from '~/logic/models/utils/GenericEntity';
import {JSONSerializableKeys} from '~/logic/models/utils/JSONSerializable';

export class StaffSpecialty extends GenericEntity<StaffSpecialty> {
    private _id: number;
    private _name: string;

    public get id(): number {
        return this._id;
    }

    public set id(value: number) {
        this._id = value;
    }

    public get name(): string {
        return this._name;
    }

    public set name(value: string) {
        this._name = value;
    }

    public toJSON(): JSONSerializableKeys<StaffSpecialty> {
        return {
            id: this.id,
            name: this.name
        };
    }
}
