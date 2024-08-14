package com.example.itemservice.repository;

import com.example.itemservice.domain.item.Category;
import com.example.itemservice.domain.item.Item;
import com.example.itemservice.domain.item.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.example.itemservice.domain.item.QItem.*;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Item> getPage(String search,Category category,int page) {

        BooleanBuilder builder = new BooleanBuilder();

        if (hasText(search)) {
            builder.and(item.name.contains(search));
        }

        if (category != null) {
            builder.and(item.category.eq(category));
        }

        log.info("search = {}", search);
        log.info("category = {}", category);


        List<Item> content = queryFactory.selectFrom(item)
                .where(builder)
                .limit(16)
                .offset((long) (page - 1) * 16)
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(item.count())
                .from(item)
                .where(builder);

        return PageableExecutionUtils.getPage(content, PageRequest.of(page-1, 16), countQuery::fetchOne);


    }

}
