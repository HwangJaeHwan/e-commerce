import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import type ItemAdd from "@/entity/item/ItemAdd";
import Item from "@/entity/item/Item";
import ItemUpdate from "@/entity/item/ItemUpdate";

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

    public update(request: ItemUpdate) {
        return this.httpRepository.patch({
            path: `/api/item-service/update/${request.itemUUID}`,
            body: request
        })
    }

}