import {GenericEntity} from '~/src/logic/models/utils/GenericEntity';
import {JSONSerializableKeys} from '~/src/logic/models/utils/JSONSerializable';

export class StaffSpecialty extends GenericEntity<StaffSpecialty> {
    private _id: string;
    private _name: string;

    public get id(): string {
        return this._id;
    }

    public set id(value: string) {
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
