package com.example.orderservice.repository;

import com.example.orderservice.domain.CartItem;
import com.example.orderservice.domain.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select i from CartItem i where i.cart =:cart and i.itemUUID =:itemUUID ")
    Optional<CartItem> findByMessage(@Param("cart") ShoppingCart cart, @Param("itemUUID") String itemUUID);


}
