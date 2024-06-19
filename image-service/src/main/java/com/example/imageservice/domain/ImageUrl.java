package com.example.imageservice.domain;

import com.example.imageservice.domain.image.Image;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.*;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ImageUrl {

    @Id
    @GeneratedValue
    @Column(name = "uri_id")
    private Long id;


    private String originalName;
    private String storedName;

    @ManyToOne(fetch = LAZY)
    private Image image;


    public ImageUrl(String originalName, String storedName) {

        this.storedName = storedName;
        this.originalName = originalName;
    }

    public void linkImage(Image image) {
        this.image = image;
    }
}
