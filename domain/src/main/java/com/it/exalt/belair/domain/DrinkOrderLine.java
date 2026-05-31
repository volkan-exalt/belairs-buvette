package com.it.exalt.belair.domain;

import java.util.Objects;

public final class DrinkOrderLine extends OrderLine {

    private final DrinkType drinkType;

    public DrinkOrderLine(String name, DrinkType drinkType, int quantity) {
        super(name, quantity);
        this.drinkType = Objects.requireNonNull(drinkType, "drinkType must not be null");
    }

    public DrinkType drinkType() {
        return drinkType;
    }

    @Override
    public TokenBalance cost() {
        return new TokenBalance(drinkType.drinkTokenCost() * quantity(), 0);
    }

    @Override
    public int preparationTime() {
        return drinkType.preparationTime();
    }

    @Override
    public boolean isSameType(OrderLine other) {
        if (!(other instanceof DrinkOrderLine)) {
            return false;
        }
        DrinkOrderLine otherDrink = (DrinkOrderLine) other;
        return drinkType == otherDrink.drinkType;
    }
}
