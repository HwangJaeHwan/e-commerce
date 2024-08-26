import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import type PaymentValidate from "@/entity/payment/PaymentValidate";

@singleton()
export default class PaymentRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository : HttpRepository) {
    }


    public validate(request:PaymentValidate) {
        return this.httpRepository.post( {
            path: '/api/pay-service/validate',
            body: request
        })
    }



}