import {APIResponse} from '~/logic/models/APIResponse';

export type PostConfig<T> = {
    params?: any;
    data: T;
    contentType: string;
    accepts: string | string[];
    paginate?: boolean;
}

export type PutConfig<T> = PostConfig<T>;

export type GetConfig<T> = Partial<Omit<PostConfig<T>, 'accepts'>> & Pick<PostConfig<T>, 'accepts'>;

export type DeleteConfig<T> = Partial<PostConfig<T>>;

export interface RestRepository {
    get<R, T = any>(path: string, config: GetConfig<T>): Promise<APIResponse<R>>;

    post<R, T>(path: string, config: PostConfig<T>): Promise<APIResponse<R>>;

    put<R, T>(path: string, config: PutConfig<T>): Promise<APIResponse<R>>;

    delete<R = any, T = any>(path: string, config?: DeleteConfig<T>): Promise<APIResponse<R>>;
}
