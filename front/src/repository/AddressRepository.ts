import type HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import type Address from "@/entity/address/Address";
import type AddressRequest from "@/entity/address/AddressRequest";


@singleton()
export default class AddressRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository : HttpRepository) {
    }


    public getAddresses() {
        return this.httpRepository.get<Address>({
            path: '/api/user-service/addresses',
        },Address)
    }

    public addAddress(request: AddressRequest) {
        return this.httpRepository.post({
            path: '/api/user-service/address/add',
            body: request
        })
    }

    public deleteAddress(id:number) {
        return this.httpRepository.delete({
            path: `api/user-service/address/${id}`
        })
    }


}