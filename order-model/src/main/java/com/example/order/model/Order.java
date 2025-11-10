package com.example.order.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Order<T extends Product> {
    private final UUID id;
    private final T product;

    @Builder
    public Order(UUID id, T product) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.product = product;
    }
}


