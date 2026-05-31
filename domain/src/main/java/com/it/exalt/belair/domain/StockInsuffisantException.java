package com.it.exalt.belair.domain;

public class StockInsuffisantException extends DomainValidationException {
    public StockInsuffisantException(Article article, int requestedQuantity, int availableQuantity) {
        super("Insufficient stock for " + article.nom() + ": requested " + requestedQuantity + ", available " + availableQuantity);
    }

    public String code() {
        return "STOCK_INSUFFISANT";
    }
}
