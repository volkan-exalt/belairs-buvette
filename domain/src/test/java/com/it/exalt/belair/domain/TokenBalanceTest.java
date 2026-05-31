package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenBalanceTest {

    @Test
    void shouldAddSubtractAndCompareBalances() {
        TokenBalance balance = new TokenBalance(6, 9);

        assertEquals(new TokenBalance(7, 11), balance.add(new TokenBalance(1, 2)));
        assertEquals(new TokenBalance(5, 7), balance.subtract(new TokenBalance(1, 2)));
        assertTrue(balance.canCover(new TokenBalance(6, 9)));
        assertFalse(balance.canCover(new TokenBalance(7, 9)));
        assertEquals(new TokenBalance(6, 9), balance);
        assertNotEquals(new TokenBalance(6, 8), balance);
        assertTrue(balance.toString().contains("drinkTokens=6"));
    }

    @Test
    void shouldValidateBalances() {
        TokenBalance balance = new TokenBalance(1, 1);

        assertThrows(DomainValidationException.class, () -> new TokenBalance(-1, 0));
        assertThrows(DomainValidationException.class, () -> balance.subtract(new TokenBalance(2, 0)));
        assertThrows(NullPointerException.class, () -> balance.add(null));
        assertThrows(NullPointerException.class, () -> balance.subtract(null));
        assertThrows(NullPointerException.class, () -> balance.canCover(null));
    }
}
