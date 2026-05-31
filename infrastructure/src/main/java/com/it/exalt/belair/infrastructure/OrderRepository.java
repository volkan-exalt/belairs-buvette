package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.Order;
import com.it.exalt.belair.domain.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findAll();

    void deleteById(UUID id);
}
