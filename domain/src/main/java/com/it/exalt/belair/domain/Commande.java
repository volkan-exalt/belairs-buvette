package com.it.exalt.belair.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public record Commande(UUID id, String festivalierId, CommandeStatus status, List<LigneCommande> lignes) {
    public Commande(UUID id, CommandeStatus status, List<LigneCommande> lignes) {
        this(id, null, status, lignes);
    }

    public Commande {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(status, "status must not be null");
        if (lignes == null || lignes.isEmpty()) {
            throw new DomainValidationException("Commande must contain at least one line");
        }
        lignes = List.copyOf(lignes);
    }
}
