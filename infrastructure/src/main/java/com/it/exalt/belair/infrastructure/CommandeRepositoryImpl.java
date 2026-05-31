package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.Commande;
import com.it.exalt.belair.domain.CommandeRepository;
import com.it.exalt.belair.domain.CommandeStatus;
import com.it.exalt.belair.domain.DomainValidationException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CommandeRepositoryImpl implements CommandeRepository {
    private final Map<UUID, Commande> commandesById = new ConcurrentHashMap<>();

    @Override
    public Commande save(Commande commande) {
        Objects.requireNonNull(commande, "commande must not be null");
        commandesById.put(commande.id(), commande);
        return commande;
    }

    @Override
    public Optional<Commande> findById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(commandesById.get(id));
    }

    @Override
    public Commande updateStatus(UUID id, CommandeStatus status) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(status, "status must not be null");
        Commande current = findById(id)
                .orElseThrow(() -> new DomainValidationException("Commande not found: " + id));
        Commande updated = new Commande(current.id(), current.festivalierId(), status, current.lignes());
        commandesById.put(id, updated);
        return updated;
    }

    @Override
    public List<Commande> findByFestivalierIdAndStatus(String festivalierId, CommandeStatus status) {
        if (festivalierId == null || festivalierId.isBlank()) {
            throw new IllegalArgumentException("festivalierId must not be blank");
        }
        Objects.requireNonNull(status, "status must not be null");
        return commandesById.values().stream()
                .filter(commande -> festivalierId.equals(commande.festivalierId()))
                .filter(commande -> commande.status() == status)
                .sorted(Comparator.comparing(Commande::id))
                .toList();
    }
}
