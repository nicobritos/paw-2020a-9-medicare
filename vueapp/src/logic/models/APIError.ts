export const ErrorMIME = 'application/vnd.error.v1+json';

export interface APISubError {
    code: number;
    message: number;
}

export class APIError {
    private readonly _code: number;
    private readonly _message: number;
    private readonly _errors: readonly APISubError[];

    constructor(code: number, message: number, errors: readonly APISubError[] = []) {
        this._code = code;
        this._message = message;
        this._errors = errors;
    }

    public get code(): number {
        return this._code;
    }

    public get message(): number {
        return this._message;
    }

    public get errors(): readonly APISubError[] {
        return this._errors;
    }
}
