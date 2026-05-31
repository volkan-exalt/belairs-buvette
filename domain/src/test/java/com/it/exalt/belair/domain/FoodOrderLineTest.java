package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FoodOrderLineTest {

    @Test
    void shouldCalculateFoodCostsAndCompareTypes() {
        FoodOrderLine chips = new FoodOrderLine("Chips", FoodType.SNACK, 2);
        FoodOrderLine burger = new FoodOrderLine("Burger", FoodType.MEAL, 1);

        assertEquals(new TokenBalance(0, 2), chips.cost());
        assertEquals(new TokenBalance(0, 3), burger.cost());
        assertEquals(FoodType.MEAL.preparationTime(), burger.preparationTime());
        assertTrue(new FoodOrderLine("Chips", FoodType.SNACK, 1).isSameType(chips));
        assertFalse(chips.isSameType(new DrinkOrderLine("Chips", DrinkType.NON_ALCOHOLIC, 1)));
    }

    @Test
    void shouldValidateFoodOrderLine() {
        assertThrows(DomainValidationException.class, () -> new FoodOrderLine("", FoodType.SNACK, 1));
        assertThrows(DomainValidationException.class, () -> new FoodOrderLine("Chips", FoodType.SNACK, 0));
        assertThrows(NullPointerException.class, () -> new FoodOrderLine("Chips", null, 1));
    }
}
