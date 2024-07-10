import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import type ItemAdd from "@/entity/item/ItemAdd";
import {plainToClass, plainToInstance} from "class-transformer";
import Item from "@/entity/item/Item";
import Paging from "@/entity/data/Paging";

@singleton()
export default class ItemRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public write(request: ItemAdd) {
        return this.httpRepository.post({
            path: "api/item-service/add",
            body: request
        })
    }

    public get(itemId: number) {
        return this.httpRepository.get({path: `api/item-service/${itemId}`},Item)

    }

    public getList() {
        return this.httpRepository.getList<Item>({
                path: '/api/item-service?page=1'
            },
            Item
        )
    }

}