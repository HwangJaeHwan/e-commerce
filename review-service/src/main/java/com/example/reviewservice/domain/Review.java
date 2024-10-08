package com.example.reviewservice.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Review {

    @Id
    @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @Lob
    private String content;
    private String userUUID;
    private double score;
    private String reviewUUID;
    private String itemUUID;
    @Column(updatable = false)
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Builder
    public Review(String content, String userUUID, double score, String reviewUUID, String itemUUID, LocalDateTime createTime,LocalDateTime updateTime) {
        this.content = content;
        this.userUUID = userUUID;
        this.score = score;
        this.reviewUUID = reviewUUID;
        this.itemUUID = itemUUID;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void updateContent(String updateContent) {
        this.content = updateContent;
    }

    public void updateScore(double updateScore) {
        this.score = updateScore;
    }

    public void changeUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
