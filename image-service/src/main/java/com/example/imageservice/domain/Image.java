package com.example.imageservice.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorColumn
public abstract class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private List<UploadImage> images;
}
