import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import Address from "@/entity/address/Address";
import AddressRequest from "@/entity/address/AddressRequest";


@singleton()
export default class AddressRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }


    public getAddresses() {
        return this.httpRepository.get<Array<Address>>({
            path: '/api/user-service/addresses',
        },Address)
    }

    public addAddress(request: AddressRequest) {
        return this.httpRepository.post({
            path: '/api/user-service/address/add',
            body: request
        })
    }

    public editAddress(id:number, request: AddressRequest){
        return this.httpRepository.patch({
            path: `/api/user-service/address/edit/${id}`,
            body: request
        })
    }

    public deleteAddress(id:number) {
        return this.httpRepository.delete({
            path: `api/user-service/address/${id}`
        })
    }


}