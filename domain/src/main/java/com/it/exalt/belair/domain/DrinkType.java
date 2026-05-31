package com.it.exalt.belair.domain;

public enum DrinkType {
    NON_ALCOHOLIC(0, 1),
    NORMAL_ALCOHOLIC(1, 2),
    PREMIUM_ALCOHOLIC(2, 3);

    private final int drinkTokenCost;
    private final int preparationTime;

    DrinkType(int drinkTokenCost, int preparationTime) {
        this.drinkTokenCost = drinkTokenCost;
        this.preparationTime = preparationTime;
    }

    public int drinkTokenCost() {
        return drinkTokenCost;
    }

    public int preparationTime() {
        return preparationTime;
    }

    public boolean isAlcoholic() {
        return this != NON_ALCOHOLIC;
    }
}
