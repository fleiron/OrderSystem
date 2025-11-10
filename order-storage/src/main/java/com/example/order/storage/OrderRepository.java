package com.example.order.storage;

import com.example.order.model.Order;
import com.example.order.model.Product;

import java.util.List;
import java.util.stream.Stream;

public interface OrderRepository {
    <T extends Product> void save(Order<T> order);
    List<Order<? extends Product>> findAll();
    <T extends Product> List<Order<T>> findByType(Class<T> productType);
    default Stream<Order<? extends Product>> stream() {
        return findAll().stream();
    }
}


