package com.example.order.storage.internal;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.storage.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

// Not exported outside module
public class InMemoryOrderRepository implements OrderRepository {
    private final ConcurrentLinkedQueue<Order<? extends Product>> data = new ConcurrentLinkedQueue<>();

    @Override
    public <T extends Product> void save(Order<T> order) {
        data.add(order);
    }

    @Override
    public List<Order<? extends Product>> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Product> List<Order<T>> findByType(Class<T> productType) {
        return data.stream()
                .filter(o -> productType.isAssignableFrom(o.getProduct().getClass()))
                .map(o -> (Order<T>) o)
                .collect(Collectors.toList());
    }
}


