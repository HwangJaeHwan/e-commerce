package com.example.orderservice.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static lombok.AccessLevel.*;

@Entity
@Getter
@Table(name = "Orders")
@ToString
@NoArgsConstructor(access = PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @OneToMany(mappedBy = "order", cascade = ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private String userUUID;
    private String orderUUID;
    private LocalDateTime orderDate;
    private String name;
    private String impUid;

    private String address;
    private String detailAddress;
    private String zipcode;
    private String phoneNumber;


    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Builder
    public Order(String userUUID, String orderUUID, LocalDateTime orderDate,
                 String name, String impUid, String address, String detailAddress,
                 String zipcode, String phoneNumber, OrderStatus orderStatus) {


        this.userUUID = userUUID;
        this.orderUUID = orderUUID;
        this.orderDate = orderDate;
        this.name = name;
        this.impUid = impUid;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.orderStatus = orderStatus;
    }

    public void addItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.linkOrder(this);
    }

    public void changeStatus(OrderStatus status) {
        this.orderStatus = status;
    }
}
