import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import ItemAdd from "@/entity/item/ItemAdd";
import Review from "@/entity/review/Review";

@singleton()
export default class ReviewRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public write(request: ItemAdd) {
        return this.httpRepository.post({
            path: "api/review-service/add",
            body: request
        })
    }

    public getList(itemUUID: string) {
        return this.httpRepository.getList<Review>({
                path: `/api/review-service/${itemUUID}`
            },
            Review
        )
    }

}