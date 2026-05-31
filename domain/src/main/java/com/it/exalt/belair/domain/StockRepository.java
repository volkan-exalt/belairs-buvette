package com.it.exalt.belair.domain;

public interface StockRepository {
    boolean exists(Article article);

    int quantiteDisponible(Article article);

    void decrementer(Article article, int quantity);
}
