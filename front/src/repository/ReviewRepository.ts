import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import ReviewList from "@/entity/review/ReviewList";
import type WriteReview from "@/entity/review/WriteReview";
import ItemUpdate from "@/entity/item/ItemUpdate";
import type ReviewUpdate from "@/entity/review/ReviewUpdate";
import Review from "@/entity/review/Review";

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

    public get(reviewId: number) {
        return this.httpRepository.get<Review>({
                path: `/api/review-service/get/${reviewId}`
            },
            Review
        )
    }

    public getList(itemUUID: string) {
        return this.httpRepository.getList<ReviewList>({
                path: `/api/review-service/${itemUUID}`
            },
            ReviewList
        )
    }

    public update(request: ReviewUpdate, reviewId:number) {
        return this.httpRepository.patch({
            path: `/api/review-service/update/${reviewId}`,
            body: request
        })
    }

}