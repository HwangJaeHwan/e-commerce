import {ElMessage} from "element-plus";
import HttpError from "@/http/HttpError";
import type {AxiosInstance, AxiosRequestConfig, AxiosResponse} from "axios";
import axios from "axios";

export type HttpRequestConfig = {
    path: string
    method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'
    params?: any
    body?: any
}

export default class AxiosHttpClient {

    private readonly client: AxiosInstance = axios.create({
        timeout: 3000,
        timeoutErrorMessage: '네트워크 상태가 좋지 않습니다.',
    })


    private async request(config:HttpRequestConfig) {
        return this.client
            .request({
                method: config.method,
                url: config.path,
                params: config.params,
                data: config.body,
            })
            .then((response:AxiosResponse) => {
                return response.data
            })

            .catch(e => {
                return Promise.reject(new HttpError(e))
            })
    }

    public async get(config: HttpRequestConfig) {
        return this.request({ ...config, method: 'GET'})
    }

    public async post(config: HttpRequestConfig) {
        return this.request({ ...config, method: 'POST'})
    }

    public async patch(config: HttpRequestConfig) {
        return this.request({ ...config, method: 'PATCH'})
    }

    public async delete(config: HttpRequestConfig) {
        return this.request({ ...config, method: 'DELETE'})
    }

}