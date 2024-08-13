import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import type ItemAdd from "@/entity/item/ItemAdd";
import Item from "@/entity/item/Item";

@singleton()
export default class ItemRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public write(request: ItemAdd) {
        return this.httpRepository.post({
            path: "/api/item-service/add",
            body: request
        })
    }

    public get(itemUUID: string) {
        return this.httpRepository.get<Item>({path: `/api/item-service/${itemUUID}`},Item)

    }

    public getList(params: string) {
        return this.httpRepository.getList<Item>({
                path: '/api/item-service/items?'+params
            },
            Item
        )
    }

}