package com.example.reviewservice.repository;

import com.example.reviewservice.domain.Review;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.reviewservice.domain.QReview.*;

@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public Double averageScore(String itemUUID) {
        return queryFactory.select(review.score.avg())
                .from(review)
                .where(review.itemUUID.eq(itemUUID))
                .fetchOne();
    }
    @Override
    public Page<Review> getPage(int page, String itemUUID) {

        List<Review> content = queryFactory.selectFrom(review)
                .where(review.itemUUID.eq(itemUUID))
                .limit(10)
                .orderBy(review.createTime.desc())
                .offset((long)(page - 1) * 10)
                .fetch();


        JPAQuery<Long> countQuery = queryFactory.select(review.count())
                .from(review)
                .where(review.itemUUID.eq(itemUUID));

        return PageableExecutionUtils.getPage(content, PageRequest.of(page-1, 10), countQuery::fetchOne);

    }

}
