import {inject, singleton} from "tsyringe";
import axios from "axios";
import Item from "@/entity/item/Item";
import HttpRepository from "@/repository/HttpRepository";

@singleton()
export default class ImageRepository {


    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }


    public upload(request: FormData) {

       return this.httpRepository.post({
           path:'/api/image-service/upload',
           body: request
       })
    }

    public get(filename: string) {
        return axios.get(`api/image-service/${filename}`,{ responseType: 'blob' })

    }


}