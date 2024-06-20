package com.example.itemservice.repository;

import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.itemservice.domain.item.QItem.*;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class ItemRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public Page<Item> getPage(String search, Category category, int page) {

        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(search)) {
            builder.and(item.name.contains(search));
        }

        if (category != null) {
            builder.and(item.category.eq(category));
        }


        List<Item> content = queryFactory.selectFrom(item)
                .where(builder)
                .limit(10)
                .offset((long) (page - 1) * 10)
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(item.count())
                .from(item)
                .where(builder);

        return PageableExecutionUtils.getPage(content, PageRequest.of(page-1, 10), countQuery::fetchOne);


    }
}
