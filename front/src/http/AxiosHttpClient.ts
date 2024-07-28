import HttpError from "@/http/HttpError";
import type {AxiosInstance, AxiosResponse} from "axios";
import axios from "axios";
import {singleton} from "tsyringe";

export type HttpRequestConfig = {
    path: string
    method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'
    params?: any
    body?: any
    headers?: { [key: string]: string }
}

@singleton()
export default class AxiosHttpClient {

    private readonly client: AxiosInstance = axios.create({
        timeout: 3000,
        timeoutErrorMessage: '네트워크 상태가 좋지 않습니다.',
        headers: {
            'Authorization': localStorage.getItem("token")
        }
    })


    public async request(config:HttpRequestConfig) {

        return this.client
            .request({
                method: config.method,
                url: config.path,
                params: config.params,
                data: config.body,
                headers: config.headers,
            })
            .then((response:AxiosResponse) => {
                return response.data
            })

            .catch(e => {
                return Promise.reject(new HttpError(e))
            });
    }


}