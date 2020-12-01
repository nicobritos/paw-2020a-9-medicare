import {GenericEntity} from '~/src/logic/models/utils/GenericEntity';
import {JSONSerializableKeys} from '~/src/logic/models/utils/JSONSerializable';
import {Locality} from '~/src/logic/models/Locality';

export class Office extends GenericEntity<Office> {
    private _id: number;
    private _name: string;
    private _phone: string;
    private _email: string;
    private _street: string;
    private _url: string;
    private _locality: Locality;

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

    public get phone(): string {
        return this._phone;
    }

    public set phone(value: string) {
        this._phone = value;
    }

    public get email(): string {
        return this._email;
    }

    public set email(value: string) {
        this._email = value;
    }

    public get street(): string {
        return this._street;
    }

    public set street(value: string) {
        this._street = value;
    }

    public get url(): string {
        return this._url;
    }

    public set url(value: string) {
        this._url = value;
    }

    public get locality(): Locality {
        return this._locality;
    }

    public set locality(value: Locality) {
        this._locality = value;
    }

    public toJSON(): JSONSerializableKeys<Office> {
        return {
            id: this.id,
            email: this.email,
            locality: this.locality,
            name: this.name,
            phone: this.phone,
            street: this.street,
            url: this.url
        };
    }
}
