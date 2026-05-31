package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class DomainValidationExceptionTest {

    @Test
    void shouldKeepMessageAndCause() {
        RuntimeException cause = new RuntimeException("cause");
        DomainValidationException exception = new DomainValidationException("message", cause);

        assertEquals("message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}
