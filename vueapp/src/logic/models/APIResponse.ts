import {APIError} from '~/logic/models/APIError';

export abstract class APIResponseFactory {
    public static error<T>(error: APIError): APIResponse<T> {
        return new APIResponse<T>(undefined, error)
    }

    public static ok<T>(data: T): APIResponse<T> {
        return new APIResponse<T>(data, undefined);
    }
}

export class APIResponse<T> {
    private readonly _data?: T;
    private readonly _error?: APIError;

    constructor(data: T | undefined, error: APIError | undefined) {
        this._data = data;
        this._error = error;
    }

    public get data(): T | undefined {
        return this._data;
    }

    public get error(): APIError | undefined {
        return this._error;
    }

    public isOk(): boolean {
        return this._error === undefined;
    }
}
