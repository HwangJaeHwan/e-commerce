import HttpRepository from "@/repository/HttpRepository";
import Order from "@/entity/order/Order";
import {inject, singleton} from "tsyringe";
import type OrderRequest from "@/entity/order/OrderRequest";
import Number from "@/entity/data/Number";

@singleton()
export default class OrderRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository : HttpRepository) {
    }


    public createOrder(request:OrderRequest) {
        return this.httpRepository.post( {
            path: '/api/order-service/orders',
            body: request
        })
    }

    public getOrder(orderId:number) {
        return this.httpRepository.get<Order>({
            path: `/api/order-service/orders/${orderId}`
        },Order)
    }

    public getOrders(page: number) {
        return this.httpRepository.getList<Order>({
            path: `/api/order-service/orders?page=${page}`
        }, Order);
    }

    public cancelOrder(orderId:number) {
        return this.httpRepository.patch<Order>({
            path: `/api/order-service/${orderId}`
        })
    }




}