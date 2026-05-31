package com.it.exalt.belair.domain;

import java.util.Objects;

public abstract class OrderLine {
    private final String name;
    private final int quantity;

    protected OrderLine(String name, int quantity) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Order line name must not be blank");
        }
        if (quantity <= 0) {
            throw new DomainValidationException("Order quantity must be positive");
        }
        this.name = name.trim();
        this.quantity = quantity;
    }

    public String name() {
        return name;
    }

    public int quantity() {
        return quantity;
    }

    public abstract TokenBalance cost();

    public abstract int preparationTime();

    public abstract boolean isSameType(OrderLine other);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLine)) return false;
        OrderLine orderLine = (OrderLine) o;
        return name.equals(orderLine.name) && getClass().equals(orderLine.getClass()) && isSameType(orderLine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getClass());
    }
}
