import {JSONSerializable, JSONSerializableKeys} from '~/src/logic/models/utils/JSONSerializable';

export const ErrorMIME = 'application/vnd.error.v1+json';

export class APIError implements JSONSerializable<APIError> {
    private readonly _code: number;
    private readonly _message: number;

    constructor(code: number, message: number) {
        this._code = code;
        this._message = message;
    }

    public get code(): number {
        return this._code;
    }

    public get message(): number {
        return this._message;
    }

    public toJSON(): JSONSerializableKeys<APIError> {
        return {
            code: this.code,
            message: this.message
        };
    }
}
