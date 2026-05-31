package com.it.exalt.belair.domain;

public class ArticleInconnuException extends DomainValidationException {
    public ArticleInconnuException(Article article) {
        super("Unknown article: " + article.nom());
    }

    public String code() {
        return "ARTICLE_INCONNU";
    }
}
