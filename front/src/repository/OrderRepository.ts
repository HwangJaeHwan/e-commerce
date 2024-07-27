import HttpRepository from "@/repository/HttpRepository";
import type Order from "@/entity/order/Order";
import {inject, singleton} from "tsyringe";

@singleton()
export default class OrderRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository : HttpRepository) {
    }


    public getOrders() {
        return this.httpRepository.get<Order>({
            path: "/api/order-service/orders"
        },Order)
    }




}