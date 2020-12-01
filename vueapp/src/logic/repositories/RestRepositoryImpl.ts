import axios, {AxiosRequestConfig, AxiosResponse} from 'axios';
import {
    DeleteConfig,
    GetConfig,
    PostConfig,
    PutConfig,
    RestRepository,
} from '~/src/logic/interfaces/repositories/RestRepository';
import {APIError, ErrorMIME} from '~/src/logic/models/APIError';
import {APIResponse, APIResponseFactory} from '~/src/logic/models/APIResponse';
import {Pagination, PaginationLinks} from '~/src/logic/models/utils/Pagination';
const parseLinkHeader = require('parse-link-header');

export class RestRepositoryImpl implements RestRepository {
    public async get<R, T = any>(path: string, config: GetConfig<T>): Promise<APIResponse<R>> {
        let axiosConfig = RestRepositoryImpl.getAxiosConfig(config);
        return RestRepositoryImpl.formatResponse(await axios.get(RestRepositoryImpl.formatPath(path), axiosConfig));
    }

    public async post<R, T>(path: string, config: PostConfig<T>): Promise<APIResponse<R>> {
        let axiosConfig = RestRepositoryImpl.getAxiosConfig(config);
        return RestRepositoryImpl.formatResponse(await axios.post(RestRepositoryImpl.formatPath(path), axiosConfig));
    }

    public async put<R, T>(path: string, config: PutConfig<T>): Promise<APIResponse<R>> {
        let axiosConfig = RestRepositoryImpl.getAxiosConfig(config);
        return RestRepositoryImpl.formatResponse(await axios.put(RestRepositoryImpl.formatPath(path), axiosConfig));
    }

    public async delete<R = any, T = any>(path: string, config?: DeleteConfig<T>): Promise<APIResponse<R>> {
        let axiosConfig = RestRepositoryImpl.getAxiosConfig(config || {});
        return RestRepositoryImpl.formatResponse(await axios.delete(RestRepositoryImpl.formatPath(path), axiosConfig));
    }

    private static formatPath(path: string): string {
        // TODO: Agarrar esto del .env
        // FIXME: antes de produccion
        return '/api/' + path;
    }

    private static getAxiosConfig<R>(config: GetConfig<R> | PostConfig<R> | PutConfig<R> | DeleteConfig<R>): AxiosRequestConfig {
        let axiosConfig: AxiosRequestConfig = {
            headers: {
                Accept: RestRepositoryImpl.getAccept(config.accepts)
            },
            transformResponse: (data, headers) => {
                if (typeof data === 'string' && typeof headers['content-type'] === 'string' && headers['content-type'].endsWith('+json')) {
                    data = JSON.parse(data);
                }

                if (config.paginate)
                    return new Pagination(data, headers['total-items'] || 0, RestRepositoryImpl.parseLinkHeader(headers));
                else
                    return data;
            }
        };

        if (config.contentType)
            RestRepositoryImpl.setContentType(config, config.contentType);
        if (config.data)
            axiosConfig.data = config.data;

        return config;
    }

    private static setContentType(config: AxiosRequestConfig, contentType: string): void {
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
        if (typeof response.headers['content-type'] === 'string' && response.headers['content-type'] === ErrorMIME) {
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
}
