package com.it.exalt.belair.domain;

import java.util.Objects;

public final class TokenBalance {
    private final int drinkTokens;
    private final int foodTokens;

    public TokenBalance(int drinkTokens, int foodTokens) {
        if (drinkTokens < 0 || foodTokens < 0) {
            throw new DomainValidationException("Token balances cannot be negative");
        }
        this.drinkTokens = drinkTokens;
        this.foodTokens = foodTokens;
    }

    public int drinkTokens() {
        return drinkTokens;
    }

    public int foodTokens() {
        return foodTokens;
    }

    public TokenBalance add(TokenBalance other) {
        Objects.requireNonNull(other, "other balance must not be null");
        return new TokenBalance(drinkTokens + other.drinkTokens, foodTokens + other.foodTokens);
    }

    public TokenBalance subtract(TokenBalance other) {
        Objects.requireNonNull(other, "other balance must not be null");
        int remainingDrink = drinkTokens - other.drinkTokens;
        int remainingFood = foodTokens - other.foodTokens;
        if (remainingDrink < 0 || remainingFood < 0) {
            throw new DomainValidationException("Insufficient token balance");
        }
        return new TokenBalance(remainingDrink, remainingFood);
    }

    public boolean canCover(TokenBalance cost) {
        Objects.requireNonNull(cost, "cost must not be null");
        return drinkTokens >= cost.drinkTokens && foodTokens >= cost.foodTokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenBalance)) return false;
        TokenBalance that = (TokenBalance) o;
        return drinkTokens == that.drinkTokens && foodTokens == that.foodTokens;
    }

    @Override
    public int hashCode() {
        return Objects.hash(drinkTokens, foodTokens);
    }

    @Override
    public String toString() {
        return "TokenBalance{" +
                "drinkTokens=" + drinkTokens +
                ", foodTokens=" + foodTokens +
                '}';
    }
}
