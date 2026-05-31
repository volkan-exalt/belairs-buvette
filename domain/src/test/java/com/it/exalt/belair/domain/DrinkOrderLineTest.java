package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DrinkOrderLineTest {

    @Test
    void shouldCalculateDrinkCostsAndCompareTypes() {
        DrinkOrderLine water = new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 3);
        DrinkOrderLine beer = new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 2);
        DrinkOrderLine cocktail = new DrinkOrderLine("Cocktail", DrinkType.PREMIUM_ALCOHOLIC, 1);

        assertEquals(new TokenBalance(0, 0), water.cost());
        assertEquals(new TokenBalance(2, 0), beer.cost());
        assertEquals(new TokenBalance(2, 0), cocktail.cost());
        assertEquals(DrinkType.NORMAL_ALCOHOLIC.preparationTime(), beer.preparationTime());
        assertTrue(new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 1).isSameType(beer));
        assertFalse(beer.isSameType(new FoodOrderLine("Beer", FoodType.SNACK, 1)));
        assertEquals(new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 1), beer);
    }

    @Test
    void shouldValidateDrinkOrderLine() {
        assertThrows(DomainValidationException.class, () -> new DrinkOrderLine("", DrinkType.NORMAL_ALCOHOLIC, 1));
        assertThrows(DomainValidationException.class, () -> new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 0));
        assertThrows(NullPointerException.class, () -> new DrinkOrderLine("Beer", null, 1));
    }
}
