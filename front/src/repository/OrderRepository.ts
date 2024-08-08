import HttpRepository from "@/repository/HttpRepository";
import Order from "@/entity/order/Order";
import {inject, singleton} from "tsyringe";

@singleton()
export default class OrderRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository : HttpRepository) {
    }


    public getOrders() {
        return this.httpRepository.getList<Order>({
            path: "/api/order-service/orders"
        },Order)
    }




}