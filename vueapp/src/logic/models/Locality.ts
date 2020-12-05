import {GenericEntity} from '~/logic/models/utils/GenericEntity';
import {JSONSerializableKeys} from '~/logic/models/utils/JSONSerializable';
import {Province} from '~/logic/models/Province';

export class Locality extends GenericEntity<Locality> {
    private _id: number;
    private _name: string;
    private _province: Province;

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
