package com.it.exalt.belair.domain;

import java.util.Objects;

public record LigneCommande(Article article, int quantite) {
    public LigneCommande {
        Objects.requireNonNull(article, "article must not be null");
        if (quantite <= 0) {
            throw new DomainValidationException("Order line quantity must be positive");
        }
    }
}
