package com.it.exalt.belair.domain;

import java.util.List;
import java.util.Objects;

public class OrderChangeRequest {
    private final Order order;
    private final List<OrderLine> proposedLines;
    private boolean approved;
    private boolean rejected;

    public OrderChangeRequest(Order order, List<OrderLine> proposedLines) {
        this.order = Objects.requireNonNull(order, "order must not be null");
        if (proposedLines == null || proposedLines.isEmpty()) {
            throw new DomainValidationException("A change request requires at least one proposed order line");
        }
        this.proposedLines = List.copyOf(proposedLines);
        this.approved = false;
        this.rejected = false;
    }

    public List<OrderLine> proposedLines() {
        return proposedLines;
    }

    public boolean isApproved() {
        return approved;
    }

    public boolean isRejected() {
        return rejected;
    }

    public void approve() {
        if (approved || rejected) {
            throw new DomainValidationException("Change request has already been completed");
        }
        if (!order.hasTransferablePreparedItem(proposedLines)) {
            throw new DomainValidationException("The change request cannot be accepted because no prepared item can be transferred");
        }
        order.applyChange(proposedLines);
        this.approved = true;
    }

    public void reject() {
        if (approved || rejected) {
            throw new DomainValidationException("Change request has already been completed");
        }
        this.rejected = true;
    }
}
