import {inject, singleton} from "tsyringe";
import axios from "axios";
import Item from "@/entity/item/Item";
import HttpRepository from "@/repository/HttpRepository";
import ImageResponse from "@/entity/image/ImageResponse";
import type ImageListRequest from "@/entity/image/ImageListRequest";

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
        return axios.get(`/api/image-service/${filename}`,{ responseType: 'blob' })

    }

    public getItemImage(itemUUID: string) {
        // return axios.get(`/api/image-service/images/${itemUUID}`)
        return this.httpRepository.get<ImageResponse>({
            path:`/api/image-service/images/item/${itemUUID}`
        },ImageResponse)

    }


    public getReviewImage(reviewUUID: string) {
        // return axios.get(`/api/image-service/images/${itemUUID}`)
        return this.httpRepository.get<ImageResponse>({
            path:`/api/image-service/images/review/${reviewUUID}`
        },ImageResponse)

    }

    public getImages(request: ImageListRequest){

        return this.httpRepository.post<Array<ImageResponse>>({
            path:'/api/image-service/images',
            body: request,
        },Array<ImageResponse>)
    }

    public update(request: FormData, reviewUUID: string) {

        return this.httpRepository.post({
            path:`/api/image-service/update/${reviewUUID}`,
            body: request
        })
    }


}