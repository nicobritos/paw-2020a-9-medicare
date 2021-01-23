import axios, {AxiosRequestConfig, AxiosResponse} from 'axios';
import {
    DeleteConfig,
    GetConfig,
    LOGGED_IN_TTL_HEADER_NAME,
    PostConfig,
    PutConfig,
    RestRepository,
    StatusCodes,
} from '~/logic/interfaces/repositories/RestRepository';
import {APIError, ErrorMIME} from '~/logic/models/APIError';
import {APIResponse, APIResponseFactory} from '~/logic/models/APIResponse';
import {Pagination, PaginationLinks} from '~/logic/models/utils/Pagination';
import parseLinkHeader from 'parse-link-header';
import {injectable} from 'inversify';
import {createApiPath} from '../Utils';
import {UserDoctors, UserPatients} from '~/logic/interfaces/services/AuthService';
import {UserMIME} from '~/logic/services/UserServiceImpl';
import {JSON_MIME} from '~/logic/services/Utils';
import store from '~/plugins/vuex';
import {authMutationTypes, LOGGED_IN_EXPIRATION_DATE_KEY} from '~/store/types/auth.types';
import EventBus from '~/logic/EventBus';
import {APIErrorEventName} from '~/logic/interfaces/APIErrorEvent';

@injectable()
export class RestRepositoryImpl implements RestRepository {
    private static readonly REFRESH_PATH = 'auth/refresh';

    public async get<R, T = any>(path: string, config: GetConfig<T>): Promise<APIResponse<R>> {
        let axiosConfig = RestRepositoryImpl.getAxiosConfig(config);
        return RestRepositoryImpl.createResponse(config.retry, () => axios.get(createApiPath(path), axiosConfig));
    }

    public async post<R, T>(path: string, config: PostConfig<T>): Promise<APIResponse<R>> {
        let axiosConfig = RestRepositoryImpl.getAxiosConfig(config);
        return RestRepositoryImpl.createResponse(config.retry, () => axios.post(createApiPath(path), axiosConfig.data, axiosConfig));
    }

    public async put<R, T>(path: string, config: PutConfig<T>): Promise<APIResponse<R>> {
        let axiosConfig = RestRepositoryImpl.getAxiosConfig(config);
        return RestRepositoryImpl.createResponse(config.retry, () => axios.put(createApiPath(path), axiosConfig.data, axiosConfig));
    }

    public async delete<R = any, T = any>(path: string, config?: DeleteConfig<T>): Promise<APIResponse<R>> {
        let axiosConfig = RestRepositoryImpl.getAxiosConfig(config || {});
        return RestRepositoryImpl.createResponse(config?.retry, () => axios.delete(createApiPath(path), axiosConfig));
    }

    private static async createResponse<R>(retry: boolean | undefined, runAction: () => Promise<AxiosResponse<R>>): Promise<APIResponse<R>> {
        let apiResponse = RestRepositoryImpl.formatResponse<R>(await runAction());
        // We want to retry on undefined (default behaviour)
        if (!apiResponse.isOk && apiResponse.error!.code === StatusCodes.UNAUTHORIZED && retry !== false) {
            if (!await RestRepositoryImpl.refreshToken())
                return apiResponse;

            // Redo with freshly generated JWT
            return RestRepositoryImpl.formatResponse<R>(await runAction());
        } else {
            return apiResponse;
        }
    }

    private static async refreshToken(): Promise<boolean> {
        let axiosConfig = RestRepositoryImpl.getAxiosConfig({
            accepts: UserMIME.ME,
            data: undefined,
            contentType: JSON_MIME
        });

        let response = RestRepositoryImpl.formatResponse(
            await axios.post(
                createApiPath(RestRepositoryImpl.REFRESH_PATH),
                undefined,
                axiosConfig
            )
        );
        if (response.isOk) {
            store.commit('auth/setUser', authMutationTypes.setUser((response.data as UserDoctors | UserPatients).user));

            if ((response.data as UserDoctors).doctors != null) {
                store.commit('auth/setDoctors', authMutationTypes.setDoctors((response.data as UserDoctors).doctors));
            } else {
                store.commit('auth/setPatients', authMutationTypes.setPatients((response.data as UserPatients).patients));
            }
        }

        return response.isOk;
    }

    private static getAxiosConfig<R>(config: GetConfig<R> | PostConfig<R> | PutConfig<R> | DeleteConfig<R>): AxiosRequestConfig {
        let axiosConfig: AxiosRequestConfig = {
            headers: {
                Accept: RestRepositoryImpl.getAccept(config.accepts)
            },
            transformResponse: (data: any, headers) => {
                let ttl = headers[LOGGED_IN_TTL_HEADER_NAME];
                if (ttl) {
                    let expDate = (new Date(0)).getTime() + Number.parseInt(ttl);
                    localStorage.setItem(LOGGED_IN_EXPIRATION_DATE_KEY, expDate.toString());
                }

                let contentType = RestRepositoryImpl.parseContentType(headers);

                if (typeof data === 'string' && contentType.endsWith('+json')) {
                    data = JSON.parse(data);
                }

                if (config.paginate && data && !contentType.startsWith(ErrorMIME))
                    return new Pagination(data, headers['total-items'] || 0, RestRepositoryImpl.parseLinkHeader(headers));
                else
                    return data;
            },
            validateStatus: () => {
                // Returns true because we want to manage API errors using the APIError class.
                // Without this function axios throws an error if status code is between 200 and 300
                return true;
            }
        };

        if (config.contentType)
            RestRepositoryImpl.setContentType(axiosConfig, config.contentType);
        if (config.data)
            axiosConfig.data = config.data;
        if (config.params)
            axiosConfig.params = config.params;

        return axiosConfig;
    }

    private static setContentType(config: AxiosRequestConfig, contentType: string): void {
        if (!config.headers)
            config.headers = {};
        config.headers['Content-Type'] = contentType;
    }

    private static getAccept(accepts: string | string[] | undefined): string {
        if (accepts) {
            return [
                ErrorMIME,
                accepts,
            ]
                .flat(1)
                .join(',');
        } else {
            return ErrorMIME;
        }
    }

    private static formatResponse<R>(response: AxiosResponse): APIResponse<R> {
        if (typeof response.headers['content-type'] === 'string' && response.headers['content-type'].startsWith(ErrorMIME)) {
            let apiError: APIError = response.data;
            if (apiError.errors && apiError.errors.length) {
                for (let error of apiError.errors)
                    EventBus.$emit(APIErrorEventName, error.code);
            }

            return APIResponseFactory.error(response.data as APIError);
        }

        return APIResponseFactory.ok(response.data);
    }

    private static parseLinkHeader(headers: any): PaginationLinks | undefined {
        let link = headers['link'];
        if (typeof link !== 'string') return undefined;

        let parsed = parseLinkHeader(link);
        if (!parsed) return undefined;

        let paginationLinks: PaginationLinks = {
            first: parsed['first'].url,
            last: parsed['last'].url
        };

        if (parsed['next']) paginationLinks.next = parsed['next'].url;
        if (parsed['previous']) paginationLinks.previous = parsed['previous'].url;

        return paginationLinks;
    }

    private static parseContentType(headers: any): string {
        let contentType = headers['content-type'];
        if (typeof contentType === 'string') {
            if (contentType.includes(';')) {
                contentType = contentType.substring(0, contentType.indexOf(';'));
            }
        }
        return contentType || '';
    }
}
