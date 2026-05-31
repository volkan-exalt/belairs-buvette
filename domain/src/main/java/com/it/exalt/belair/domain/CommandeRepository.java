package com.it.exalt.belair.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommandeRepository {
    Commande save(Commande commande);

    Optional<Commande> findById(UUID id);

    Commande updateStatus(UUID id, CommandeStatus status);

    List<Commande> findByFestivalierIdAndStatus(String festivalierId, CommandeStatus status);
}
