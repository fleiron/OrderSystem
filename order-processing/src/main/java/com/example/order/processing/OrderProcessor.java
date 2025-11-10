package com.example.order.processing;

import com.example.order.model.Order;
import com.example.order.model.Product;

import java.util.Objects;
import java.util.function.Consumer;

public class OrderProcessor<T extends Product> {
    private final Consumer<Order<T>> onProcessedCallback;

    public OrderProcessor(Consumer<Order<T>> onProcessedCallback) {
        this.onProcessedCallback = onProcessedCallback;
    }

    public void process(Order<T> order) {
        validate(order);
        // Simulate processing...
        onProcessedCallback.accept(order);
    }

    private void validate(Order<T> order) {
        if (order == null || order.getProduct() == null) {
            throw new ProcessingException("Order or product must not be null");
        }
        if (Objects.requireNonNull(order.getProduct()).getPrice() <= 0) {
            throw new ProcessingException("Product price must be positive: " + order.getProduct().getName());
        }
    }
}


