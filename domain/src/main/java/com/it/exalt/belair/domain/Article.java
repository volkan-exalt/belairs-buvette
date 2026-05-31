package com.it.exalt.belair.domain;

public record Article(String nom) {
    public Article {
        if (nom == null || nom.isBlank()) {
            throw new DomainValidationException("Article name must not be blank");
        }
        nom = nom.trim();
    }
}
