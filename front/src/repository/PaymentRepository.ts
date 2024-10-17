import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import type OrderRequest from "@/entity/order/OrderRequest";

@singleton()
export default class PaymentRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository : HttpRepository) {
    }


    public validate(request:OrderRequest) {
        return this.httpRepository.post( {
            path: '/api/pay-service/validate',
            body: request
        })
    }



}