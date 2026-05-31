package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DrinkTypeTest {

    @Test
    void shouldExposeDrinkCostsPreparationTimesAndAlcoholFlag() {
        assertEquals(0, DrinkType.NON_ALCOHOLIC.drinkTokenCost());
        assertEquals(1, DrinkType.NON_ALCOHOLIC.preparationTime());
        assertFalse(DrinkType.NON_ALCOHOLIC.isAlcoholic());

        assertEquals(1, DrinkType.NORMAL_ALCOHOLIC.drinkTokenCost());
        assertEquals(2, DrinkType.NORMAL_ALCOHOLIC.preparationTime());
        assertTrue(DrinkType.NORMAL_ALCOHOLIC.isAlcoholic());

        assertEquals(2, DrinkType.PREMIUM_ALCOHOLIC.drinkTokenCost());
        assertEquals(3, DrinkType.PREMIUM_ALCOHOLIC.preparationTime());
        assertTrue(DrinkType.PREMIUM_ALCOHOLIC.isAlcoholic());
    }
}
