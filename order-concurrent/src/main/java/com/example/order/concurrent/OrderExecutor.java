package com.example.order.concurrent;

import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.processing.OrderProcessor;
import com.example.order.storage.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class OrderExecutor implements AutoCloseable {
    private final ExecutorService executorService;

    public OrderExecutor(int poolSize) {
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    public <T extends Product> List<CompletableFuture<Void>> submitBatch(
            List<Order<T>> orders,
            Consumer<Order<T>> onProcessed,
            OrderRepository repository
    ) {
        Objects.requireNonNull(orders);
        Objects.requireNonNull(onProcessed);
        Objects.requireNonNull(repository);

        OrderProcessor<T> processor = new OrderProcessor<>(order -> {
            repository.save(order);
            onProcessed.accept(order);
        });

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (Order<T> order : orders) {
            CompletableFuture<Void> f = CompletableFuture.runAsync(() -> processor.process(order), executorService);
            futures.add(f);
        }
        return futures;
    }

    @Override
    public void close() {
        executorService.shutdown();
    }
}


