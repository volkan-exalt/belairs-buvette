package com.it.exalt.belair.application;

import java.util.List;

public record CreerCommandeCommand(String festivalierId, List<ArticleCommande> articles) {
    public CreerCommandeCommand {
        articles = List.copyOf(articles);
    }
}
