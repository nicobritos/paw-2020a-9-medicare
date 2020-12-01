import {GenericEntity} from '~/src/logic/models/utils/GenericEntity';
import {JSONSerializableKeys} from '~/src/logic/models/utils/JSONSerializable';
import {Province} from '~/src/logic/models/Province';

export class Locality extends GenericEntity<Locality> {
    private _id: string;
    private _name: string;
    private _province: Province;

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

    public get province(): Province {
        return this._province;
    }

    public set province(value: Province) {
        this._province = value;
    }

    public toJSON(): JSONSerializableKeys<Locality> {
        return {
            id: this.id,
            name: this.name,
            province: this.province
        };
    }
}
