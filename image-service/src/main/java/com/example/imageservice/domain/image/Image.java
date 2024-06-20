package com.example.imageservice.domain.image;


import com.example.imageservice.domain.ImageUrl;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@DiscriminatorColumn(name = "type")
public abstract class Image {

    @Id
    @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String userUUID;

    @OneToMany(mappedBy = "uri_id",cascade = CascadeType.ALL)
    private List<ImageUrl> images = new ArrayList<>();


    public void addUrl(ImageUrl imageUrl) {
        this.images.add(imageUrl);
        imageUrl.linkImage(this);

    }

    public Image(String userUUID) {
        this.userUUID = userUUID;
    }
}
