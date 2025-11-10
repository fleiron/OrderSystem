package com.example.order.app;

import com.example.order.concurrent.OrderExecutor;
import com.example.order.model.Clothing;
import com.example.order.model.Electronics;
import com.example.order.model.Order;
import com.example.order.model.Product;
import com.example.order.storage.OrderRepository;
import com.example.order.storage.Repositories;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        Faker faker = new Faker(new Locale("uk"));

        List<Order<? extends Product>> allOrders = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Electronics e = Electronics.builder()
                    .name(faker.commerce().productName())
                    .price(positivePrice(faker.number().randomDouble(2, 100, 5000)))
                    .brand(faker.company().name())
                    .model("Model-" + faker.number().digits(3))
                    .warrantyMonths(faker.number().numberBetween(6, 36))
                    .build();
            allOrders.add(Order.<Electronics>builder().product(e).build());
        }
        for (int i = 0; i < 5; i++) {
            Clothing c = Clothing.builder()
                    .name(faker.commerce().productName())
                    .price(positivePrice(faker.number().randomDouble(2, 20, 500)))
                    .size(List.of("S", "M", "L", "XL").get(faker.number().numberBetween(0, 4)))
                    .material(faker.commerce().material())
                    .color(faker.color().name())
                    .build();
            allOrders.add(Order.<Clothing>builder().product(c).build());
        }

        // Lambda filter example
        Predicate<Order<? extends Product>> isElectronics = o -> o.getProduct() instanceof Electronics;
        List<Order<Electronics>> electronicOrders = allOrders.stream()
                .filter(isElectronics)
                .map(o -> (Order<Electronics>) o)
                .collect(Collectors.toList());

        List<Order<Clothing>> clothingOrders = allOrders.stream()
                .filter(o -> o.getProduct() instanceof Clothing)
                .map(o -> (Order<Clothing>) o)
                .collect(Collectors.toList());

        OrderRepository repository = Repositories.inMemory();

        try (OrderExecutor executor = new OrderExecutor(4)) {
            // Method reference for onProcessed callback
            List<CompletableFuture<Void>> futures1 = executor.submitBatch(
                    electronicOrders,
                    Main::logProcessed,
                    repository
            );
            List<CompletableFuture<Void>> futures2 = executor.submitBatch(
                    clothingOrders,
                    order -> logProcessed(order),
                    repository
            );

            // Wait all
            List<CompletableFuture<Void>> all = new ArrayList<>();
            all.addAll(futures1);
            all.addAll(futures2);
            CompletableFuture.allOf(all.toArray(CompletableFuture[]::new)).join();
        }

        // Print results
        System.out.println("All stored orders: " + repository.findAll().size());
        System.out.println("Electronics stored: " + repository.findByType(Electronics.class).size());
        System.out.println("Clothing stored: " + repository.findByType(Clothing.class).size());
    }

    private static <T extends Product> void logProcessed(Order<T> order) {
        System.out.printf("Processed order of %s: %s, price: %.2f, thread: %s%n",
                order.getProduct().getClass().getSimpleName(),
                order.getProduct().getName(),
                order.getProduct().getPrice(),
                Thread.currentThread().getName());
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static double positivePrice(double value) {
        return value <= 0 ? 1.0 : value;
    }
}


