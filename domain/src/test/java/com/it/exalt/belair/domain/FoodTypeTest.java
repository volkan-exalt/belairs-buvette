package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FoodTypeTest {

    @Test
    void shouldExposeFoodCostsAndPreparationTimes() {
        assertEquals(1, FoodType.SNACK.foodTokenCost());
        assertEquals(2, FoodType.SNACK.preparationTime());
        assertEquals(3, FoodType.MEAL.foodTokenCost());
        assertEquals(10, FoodType.MEAL.preparationTime());
    }
}
