import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import ItemAdd from "@/entity/item/ItemAdd";
import Review from "@/entity/review/Review";
import type WriteReview from "@/entity/review/WriteReview";

@singleton()
export default class ReviewRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public write(request: WriteReview) {
        return this.httpRepository.post({
            path: `/api/review-service/write`,
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