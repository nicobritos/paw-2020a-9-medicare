import {JSONSerializable, JSONSerializableKeys} from '~/src/logic/models/utils/JSONSerializable';

export const ErrorMIME = 'application/vnd.error.v1+json';

export class APIError implements JSONSerializable<APIError> {
    private _code: number;
    private _message: number;

    public get code(): number {
        return this._code;
    }

    public set code(value: number) {
        this._code = value;
    }

    public get message(): number {
        return this._message;
    }

    public set message(value: number) {
        this._message = value;
    }

    public toJSON(): JSONSerializableKeys<APIError> {
        return {
            code: this.code,
            message: this.message
        };
    }
}
