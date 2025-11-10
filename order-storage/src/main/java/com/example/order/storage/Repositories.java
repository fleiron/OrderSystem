package com.example.order.storage;

import com.example.order.storage.internal.InMemoryOrderRepository;

public final class Repositories {
    private Repositories() {}
    public static OrderRepository inMemory() {
        return new InMemoryOrderRepository();
    }
}


