package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.DrinkOrderLine;
import com.it.exalt.belair.domain.DrinkType;
import com.it.exalt.belair.domain.FestivalGoer;
import com.it.exalt.belair.domain.Order;
import com.it.exalt.belair.domain.OrderStatus;
import com.it.exalt.belair.domain.TokenBalance;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryOrderRepositoryTest {

    @Test
    void shouldSaveFindListAndDeleteOrders() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(6, 9));
        Order first = alice.placeOrder(List.of(new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 1)));
        Order second = alice.placeOrder(List.of(new DrinkOrderLine("Coca", DrinkType.NON_ALCOHOLIC, 1)));

        repository.save(second);
        repository.save(first);

        assertSame(first, repository.findById(first.id()).orElseThrow());
        assertEquals(List.of(first, second), repository.findAll());
        assertEquals(List.of(first, second), repository.findByStatus(OrderStatus.CREATED));
        repository.deleteById(first.id());
        assertFalse(repository.findById(first.id()).isPresent());
        assertFalse(repository.findById(UUID.randomUUID()).isPresent());
    }

    @Test
    void shouldValidateInputs() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();

        assertThrows(NullPointerException.class, () -> repository.save(null));
        assertThrows(NullPointerException.class, () -> repository.findById(null));
        assertThrows(NullPointerException.class, () -> repository.findByStatus(null));
        assertThrows(NullPointerException.class, () -> repository.deleteById(null));
    }
}
