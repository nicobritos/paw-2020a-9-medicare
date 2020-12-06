export type ID = number | string;

export type Nullable<T> = null | T;

export interface Hash<T> {
    [k: string]: T
}
