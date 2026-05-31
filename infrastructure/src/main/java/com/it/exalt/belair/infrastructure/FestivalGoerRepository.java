package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;

import java.util.List;
import java.util.Optional;

public interface FestivalGoerRepository {
    FestivalGoer save(FestivalGoer festivalGoer);

    Optional<FestivalGoer> findByName(String name);

    List<FestivalGoer> findAll();

    void deleteByName(String name);
}
