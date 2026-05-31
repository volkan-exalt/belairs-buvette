package com.it.exalt.belair.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FestivalGoer {
    private final String name;
    private TokenBalance tokenBalance;

    public FestivalGoer(String name, TokenBalance initialBalance) {
        if (name == null || name.isBlank()) {
            throw new DomainValidationException("Festival goer name must not be blank");
        }
        this.name = name.trim();
        this.tokenBalance = Objects.requireNonNull(initialBalance, "initialBalance must not be null");
    }

    public String name() {
        return name;
    }

    public TokenBalance tokenBalance() {
        return tokenBalance;
    }

    public Order placeOrder(List<OrderLine> lines) {
        Order order = new Order(this, lines);
        deductTokens(order.totalCost());
        return order;
    }

    public GroupOrder placeGroupOrder(List<OrderLine> lines, Map<FestivalGoer, TokenBalance> contributions) {
        if (contributions == null || contributions.isEmpty()) {
            throw new DomainValidationException("Group contributions are required for a group order");
        }
        return new GroupOrder(this, lines, contributions);
    }

    public TokenTransferRequest requestTokenTransfer(FestivalGoer recipient, int drinkTokens, int foodTokens) {
        return new TokenTransferRequest(this, recipient, new TokenBalance(drinkTokens, foodTokens));
    }

    public void deductTokens(TokenBalance cost) {
        tokenBalance = tokenBalance.subtract(cost);
    }

    public void addTokens(TokenBalance amount) {
        tokenBalance = tokenBalance.add(amount);
    }
}
