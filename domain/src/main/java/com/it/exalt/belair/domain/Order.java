package com.it.exalt.belair.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Order {

    private final UUID id;
    private final FestivalGoer owner;
    private List<OrderLine> lines;
    private OrderStatus status;
    private int estimatedPreparationMinutes;
    private final Map<OrderLine, Integer> preparedQuantities = new HashMap<>();

    public Order(FestivalGoer owner, List<OrderLine> lines) {
        this.id = UUID.randomUUID();
        this.owner = Objects.requireNonNull(owner, "owner must not be null");
        setLines(lines);
        this.status = OrderStatus.CREATED;
    }

    protected Order(UUID id, FestivalGoer owner, List<OrderLine> lines) {
        this.id = id;
        this.owner = Objects.requireNonNull(owner, "owner must not be null");
        setLines(lines);
        this.status = OrderStatus.CREATED;
    }

    public UUID id() {
        return id;
    }

    public FestivalGoer owner() {
        return owner;
    }

    public List<OrderLine> lines() {
        return Collections.unmodifiableList(lines);
    }

    public OrderStatus status() {
        return status;
    }

    public int estimatedPreparationMinutes() {
        return estimatedPreparationMinutes;
    }

    public TokenBalance totalCost() {
        return lines.stream()
                .map(OrderLine::cost)
                .reduce(new TokenBalance(0, 0), TokenBalance::add);
    }

    public void acknowledge() {
        if (status != OrderStatus.CREATED) {
            throw new DomainValidationException("Order can only be acknowledged once after creation");
        }
        this.estimatedPreparationMinutes = OrderPreparationEstimator.estimate(lines);
        this.status = OrderStatus.ACKNOWLEDGED;
    }

    public void prepareItem(OrderLine item, int quantity) {
        if (status != OrderStatus.ACKNOWLEDGED) {
            throw new DomainValidationException("Prepared items can be registered only after the order has been acknowledged");
        }
        if (quantity <= 0) {
            throw new DomainValidationException("Prepared quantity must be positive");
        }
        if (lines.stream().noneMatch(line -> line.equals(item))) {
            throw new DomainValidationException("Prepared item does not belong to this order");
        }
        int current = preparedQuantities.getOrDefault(item, 0);
        int target = current + quantity;
        int orderedQuantity = lines.stream()
                .filter(line -> line.equals(item))
                .mapToInt(OrderLine::quantity)
                .sum();
        preparedQuantities.put(item, Math.min(target, orderedQuantity));
    }

    public boolean canMarkReady() {
        if (status != OrderStatus.ACKNOWLEDGED) {
            return false;
        }
        return lines.stream()
                .allMatch(line -> preparedQuantities.getOrDefault(line, 0) >= line.quantity());
    }

    public void markReady() {
        if (!canMarkReady()) {
            throw new DomainValidationException("Order cannot be marked ready until all items are prepared");
        }
        this.status = OrderStatus.READY;
    }

    public void cancel() {
        if (status == OrderStatus.ACKNOWLEDGED || status == OrderStatus.READY) {
            throw new DomainValidationException("Only orders that are not yet acknowledged can be canceled");
        }
        owner.addTokens(totalCost());
        cancelWithoutRefund();
    }

    protected void cancelWithoutRefund() {
        this.status = OrderStatus.CANCELED;
    }

    public void changeItems(List<OrderLine> newLines) {
        if (status != OrderStatus.CREATED) {
            throw new DomainValidationException("Order can only be changed before it has been acknowledged");
        }
        setLines(newLines);
    }

    public OrderChangeRequest requestChange(List<OrderLine> proposedLines) {
        if (status != OrderStatus.ACKNOWLEDGED) {
            throw new DomainValidationException("Change request can only be created for acknowledged orders");
        }
        return new OrderChangeRequest(this, proposedLines);
    }

    void applyChange(List<OrderLine> newLines) {
        Map<OrderLine, Integer> filteredPreparedQuantities = new HashMap<>();
        for (OrderLine prepared : preparedQuantities.keySet()) {
            newLines.stream()
                    .filter(prepared::equals)
                    .findFirst()
                    .ifPresent(line -> filteredPreparedQuantities.put(line, preparedQuantities.get(prepared)));
        }
        this.preparedQuantities.clear();
        this.preparedQuantities.putAll(filteredPreparedQuantities);
        setLines(newLines);
        this.estimatedPreparationMinutes = OrderPreparationEstimator.estimate(lines);
    }

    boolean hasTransferablePreparedItem(List<OrderLine> proposedLines) {
        return preparedQuantities.keySet().stream()
                .anyMatch(prepared -> proposedLines.stream().anyMatch(prepared::equals));
    }

    private void setLines(List<OrderLine> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new DomainValidationException("Order must contain at least one item");
        }
        this.lines = List.copyOf(lines);
    }
}
