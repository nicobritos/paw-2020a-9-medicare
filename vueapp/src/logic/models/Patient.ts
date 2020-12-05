import {GenericEntity} from '~/logic/models/utils/GenericEntity';
import {JSONSerializableKeys} from '~/logic/models/utils/JSONSerializable';
import {User} from '~/logic/models/User';

export class Patient extends GenericEntity<Patient> {
    private _id: number;
    private _user: User;
    private _officeId: number;

    public get id(): number {
        return this._id;
    }

    public set id(value: number) {
        this._id = value;
    }

    public get user(): User {
        return this._user;
    }

    public set user(value: User) {
        this._user = value;
    }

    public get officeId(): number {
        return this._officeId;
    }

    public set officeId(value: number) {
        this._officeId = value;
    }

    public toJSON(): JSONSerializableKeys<Patient> {
        return {
            id: this.id,
            user: this.user,
            officeId: this.officeId
        };
    }
}
