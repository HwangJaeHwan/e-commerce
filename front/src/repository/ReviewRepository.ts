import HttpRepository from "@/repository/HttpRepository";
import {inject, singleton} from "tsyringe";
import Review from "@/entity/review/Review";
import type WriteReview from "@/entity/review/WriteReview";

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

    public getList(itemUUID: string) {
        return this.httpRepository.getList<Review>({
                path: `/api/review-service/${itemUUID}`
            },
            Review
        )
    }

}