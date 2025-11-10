package com.example.order.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class Electronics extends Product {
    private final String brand;
    private final String model;
    private final int warrantyMonths;

    @Builder
    public Electronics(String name, double price, String brand, String model, int warrantyMonths) {
        super(name, price);
        this.brand = brand;
        this.model = model;
        this.warrantyMonths = warrantyMonths;
    }
}


