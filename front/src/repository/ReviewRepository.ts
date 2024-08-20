import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import Review from "@/entity/review/Review";
import type WriteReview from "@/entity/review/WriteReview";
import ItemUpdate from "@/entity/item/ItemUpdate";
import type ReviewUpdate from "@/entity/review/ReviewUpdate";

@singleton()
export default class ReviewRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public write(request: WriteReview,itemUUID:string) {
        return this.httpRepository.post({
            path: `/api/review-service/write/${itemUUID}`,
            body: request
        })
    }

    public get(reviewUUID: string) {
        return this.httpRepository.get<Review>({
                path: `/api/review-service/reviews/${reviewUUID}`
            },
            Review
        )
    }

    public getList(itemUUID: string) {
        return this.httpRepository.getList<Review>({
                path: `/api/review-service/${itemUUID}`
            },
            Review
        )
    }

    public update(request: ReviewUpdate, reviewUUID:string) {
        return this.httpRepository.patch({
            path: `/api/item-service/update/${reviewUUID}`,
            body: request
        })
    }

}