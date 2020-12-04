import {GenericEntity} from '~/src/logic/models/utils/GenericEntity';
import {JSONSerializableKeys} from '~/src/logic/models/utils/JSONSerializable';
import {Country} from '~/src/logic/models/Country';

export class Province extends GenericEntity<Province> {
    private _id: number;
    private _name: string;
    private _country: Country;

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

    public get country(): Country {
        return this._country;
    }

    public set country(value: Country) {
        this._country = value;
    }

    public toJSON(): JSONSerializableKeys<Province> {
        return {
            id: this.id,
            name: this.name,
            country: this.country
        };
    }
}
