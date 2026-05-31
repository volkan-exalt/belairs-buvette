package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;
import com.it.exalt.belair.domain.TokenBalance;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InMemoryFestivalGoerRepositoryTest {

    @Test
    void shouldSaveFindListAndDeleteFestivalGoers() {
        InMemoryFestivalGoerRepository repository = new InMemoryFestivalGoerRepository();
        FestivalGoer zoe = new FestivalGoer("Zoe", new TokenBalance(1, 1));
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(1, 1));

        repository.save(zoe);
        repository.save(alice);

        assertSame(alice, repository.findByName(" alice ").orElseThrow());
        assertEquals(List.of(alice, zoe), repository.findAll());
        repository.deleteByName("zoe");
        assertFalse(repository.findByName("Zoe").isPresent());
    }

    @Test
    void shouldValidateInputs() {
        InMemoryFestivalGoerRepository repository = new InMemoryFestivalGoerRepository();

        assertThrows(NullPointerException.class, () -> repository.save(null));
        assertThrows(IllegalArgumentException.class, () -> repository.findByName(" "));
        assertThrows(IllegalArgumentException.class, () -> repository.deleteByName(null));
    }
}
