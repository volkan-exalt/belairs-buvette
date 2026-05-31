package com.it.exalt.belair.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PasserCommandeUseCase {
    private final StockRepository stockRepository;
    private final CommandeRepository commandeRepository;

    public PasserCommandeUseCase(StockRepository stockRepository) {
        this(stockRepository, null);
    }

    public PasserCommandeUseCase(StockRepository stockRepository, CommandeRepository commandeRepository) {
        this.stockRepository = Objects.requireNonNull(stockRepository, "stockRepository must not be null");
        this.commandeRepository = commandeRepository;
    }

    public Commande passerCommande(List<LigneCommande> lignes) {
        return passerCommande(null, lignes);
    }

    public Commande passerCommande(String festivalierId, List<LigneCommande> lignes) {
        if (lignes == null || lignes.isEmpty()) {
            throw new DomainValidationException("Commande must contain at least one line");
        }
        List<LigneCommande> commandeLignes = List.copyOf(lignes);
        Map<Article, Integer> requestedByArticle = aggregateQuantities(commandeLignes);

        for (Map.Entry<Article, Integer> entry : requestedByArticle.entrySet()) {
            Article article = entry.getKey();
            int requestedQuantity = entry.getValue();
            if (!stockRepository.exists(article)) {
                throw new ArticleInconnuException(article);
            }
            int availableQuantity = stockRepository.quantiteDisponible(article);
            if (availableQuantity < requestedQuantity) {
                throw new StockInsuffisantException(article, requestedQuantity, availableQuantity);
            }
        }

        requestedByArticle.forEach(stockRepository::decrementer);
        Commande commande = new Commande(UUID.randomUUID(), festivalierId, CommandeStatus.EN_ATTENTE, commandeLignes);
        return commandeRepository == null ? commande : commandeRepository.save(commande);
    }

    private Map<Article, Integer> aggregateQuantities(List<LigneCommande> lignes) {
        Map<Article, Integer> requestedByArticle = new LinkedHashMap<>();
        for (LigneCommande ligne : lignes) {
            requestedByArticle.merge(ligne.article(), ligne.quantite(), Integer::sum);
        }
        return requestedByArticle;
    }
}
