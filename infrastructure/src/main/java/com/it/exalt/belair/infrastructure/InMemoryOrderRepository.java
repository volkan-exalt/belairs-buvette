package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.Order;
import com.it.exalt.belair.domain.OrderStatus;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<UUID, Order> ordersById = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        Objects.requireNonNull(order, "order must not be null");
        ordersById.put(order.id(), order);
        return order;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(ordersById.get(id));
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        Objects.requireNonNull(status, "status must not be null");
        return ordersById.values().stream()
                .filter(order -> order.status() == status)
                .sorted(Comparator.comparing(Order::id))
                .toList();
    }

    @Override
    public List<Order> findAll() {
        return ordersById.values().stream()
                .sorted(Comparator.comparing(Order::id))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        ordersById.remove(id);
    }
}
