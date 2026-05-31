package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderPreparationEstimatorTest {

    @Test
    void shouldEstimatePreparationTime() {
        List<OrderLine> lines = List.of(
                new DrinkOrderLine("Coke", DrinkType.NON_ALCOHOLIC, 2),
                new DrinkOrderLine("Biere", DrinkType.NORMAL_ALCOHOLIC, 1),
                new FoodOrderLine("Chips", FoodType.SNACK, 1),
                new FoodOrderLine("Burger", FoodType.MEAL, 1)
        );

        assertEquals(14, OrderPreparationEstimator.estimate(lines));
        assertEquals(0, OrderPreparationEstimator.estimate(new ArrayList<>()));
        assertEquals(0, OrderPreparationEstimator.estimate(null));
    }
}
