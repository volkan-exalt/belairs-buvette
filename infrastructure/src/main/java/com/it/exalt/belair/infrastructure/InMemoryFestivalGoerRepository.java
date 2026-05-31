package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFestivalGoerRepository implements FestivalGoerRepository {
    private final Map<String, FestivalGoer> festivalGoersByName = new ConcurrentHashMap<>();

    @Override
    public FestivalGoer save(FestivalGoer festivalGoer) {
        Objects.requireNonNull(festivalGoer, "festivalGoer must not be null");
        festivalGoersByName.put(normalize(festivalGoer.name()), festivalGoer);
        return festivalGoer;
    }

    @Override
    public Optional<FestivalGoer> findByName(String name) {
        return Optional.ofNullable(festivalGoersByName.get(normalize(name)));
    }

    @Override
    public List<FestivalGoer> findAll() {
        return festivalGoersByName.values().stream()
                .sorted(Comparator.comparing(FestivalGoer::name))
                .toList();
    }

    @Override
    public void deleteByName(String name) {
        festivalGoersByName.remove(normalize(name));
    }

    private static String normalize(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return name.trim().toLowerCase();
    }
}
