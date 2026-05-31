package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class OrderStatusTest {

    @Test
    void shouldExposeExpectedStatuses() {
        assertArrayEquals(
                new OrderStatus[] {
                        OrderStatus.CREATED,
                        OrderStatus.ACKNOWLEDGED,
                        OrderStatus.READY,
                        OrderStatus.CANCELED
                },
                OrderStatus.values()
        );
    }
}
