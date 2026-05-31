package com.it.exalt.belair.domain;

public enum FoodType {
    SNACK(1, 2),
    MEAL(3, 10);

    private final int foodTokenCost;
    private final int preparationTime;

    FoodType(int foodTokenCost, int preparationTime) {
        this.foodTokenCost = foodTokenCost;
        this.preparationTime = preparationTime;
    }

    public int foodTokenCost() {
        return foodTokenCost;
    }

    public int preparationTime() {
        return preparationTime;
    }
}
