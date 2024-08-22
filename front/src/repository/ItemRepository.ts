import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import type ItemAdd from "@/entity/item/ItemAdd";
import Item from "@/entity/item/Item";
import ItemUpdate from "@/entity/item/ItemUpdate";
import Number from "@/entity/data/Number";

@singleton()
export default class ItemRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public write(request: ItemAdd) {
        return this.httpRepository.post<Number>({
            path: "/api/item-service/add",
            body: request
        },Number)
    }

    public get(itemId: number) {
        return this.httpRepository.get<Item>({path: `/api/item-service/${itemId}`},Item)

    }

    public getList(params: string) {
        return this.httpRepository.getList<Item>({
                path: '/api/item-service/items?'+params
            },
            Item
        )
    }

    public update(request: ItemUpdate, itemId:number) {
        return this.httpRepository.patch({
            path: `/api/item-service/update/${itemId}`,
            body: request
        })
    }

}