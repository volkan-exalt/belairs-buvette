package com.it.exalt.belair.domain;

import java.util.Objects;

public final class FoodOrderLine extends OrderLine {

    private final FoodType foodType;

    public FoodOrderLine(String name, FoodType foodType, int quantity) {
        super(name, quantity);
        this.foodType = Objects.requireNonNull(foodType, "foodType must not be null");
    }

    public FoodType foodType() {
        return foodType;
    }

    @Override
    public TokenBalance cost() {
        return new TokenBalance(0, foodType.foodTokenCost() * quantity());
    }

    @Override
    public int preparationTime() {
        return foodType.preparationTime();
    }

    @Override
    public boolean isSameType(OrderLine other) {
        if (!(other instanceof FoodOrderLine)) {
            return false;
        }
        FoodOrderLine otherFood = (FoodOrderLine) other;
        return foodType == otherFood.foodType;
    }
}
