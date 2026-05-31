package com.it.exalt.belair.application.api;

import java.util.List;

public record CreerCommandeRequest(String festivalierId, List<ArticleCommandeRequest> articles) {
    public CreerCommandeRequest {
        articles = articles == null ? List.of() : List.copyOf(articles);
    }
}
