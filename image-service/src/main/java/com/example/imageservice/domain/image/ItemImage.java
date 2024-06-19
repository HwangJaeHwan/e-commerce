package com.example.imageservice.domain.image;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@DiscriminatorValue("ITEM")
public class ItemImage extends Image{

    String itemUUID;

    public ItemImage(String itemUUID) {
        this.itemUUID = itemUUID;
    }
}


