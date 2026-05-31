package com.it.exalt.belair.application;

import com.it.exalt.belair.domain.Article;
import com.it.exalt.belair.domain.DrinkOrderLine;
import com.it.exalt.belair.domain.DrinkType;
import com.it.exalt.belair.domain.FestivalGoer;
import com.it.exalt.belair.domain.OrderLine;
import com.it.exalt.belair.domain.PasserCommandeUseCase;
import com.it.exalt.belair.domain.StockRepository;
import com.it.exalt.belair.domain.TokenBalance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlaceOrderUseCaseTest {

    @Test
    void shouldCreatePendingOrderWhenAvailableArticleIsOrdered() {
        // Given un festivalier identifie
        FestivalGoer festivalGoer = new FestivalGoer("Alice", new TokenBalance(6, 9));
        // And un article "Mojito" disponible en stock
        OrderLine mojito = new DrinkOrderLine("Mojito", DrinkType.NORMAL_ALCOHOLIC, 1);
        PlaceOrderUseCase placeOrderUseCase = new PlaceOrderUseCase();

        // When le festivalier passe une commande pour 1 "Mojito"
        OrderResult order = placeOrderUseCase.placeOrder(festivalGoer, List.of(mojito));

        // Then la commande est creee avec le statut "EN_ATTENTE"
        assertEquals("EN_ATTENTE", order.status());
        // And le festivalier recoit un identifiant de commande
        assertNotNull(order.orderId());
    }

    @Test
    void shouldCreateCommandeFromApiCommandUsingDomainStockRules() {
        InMemoryStockRepository stockRepository = new InMemoryStockRepository();
        InMemoryCommandeRepository commandeRepository = new InMemoryCommandeRepository();
        stockRepository.add("Mojito", 10);
        PlaceOrderUseCase placeOrderUseCase = new PlaceOrderUseCase(new PasserCommandeUseCase(stockRepository, commandeRepository));
        CreerCommandeCommand command = new CreerCommandeCommand(
                "festivalier-1",
                List.of(new ArticleCommande("Mojito", 2))
        );

        CreerCommandeResult result = placeOrderUseCase.creerCommande(command);

        assertEquals("EN_ATTENTE", result.status());
        assertNotNull(result.commandeId());
        assertEquals(8, stockRepository.quantiteDisponible(new Article("Mojito")));
        assertEquals("festivalier-1", commandeRepository.savedCommande.festivalierId());
    }

    private static final class InMemoryStockRepository implements StockRepository {
        private final Map<Article, Integer> stockByArticle = new HashMap<>();

        private void add(String articleName, int availableQuantity) {
            stockByArticle.put(new Article(articleName), availableQuantity);
        }

        @Override
        public boolean exists(Article article) {
            return stockByArticle.containsKey(article);
        }

        @Override
        public int quantiteDisponible(Article article) {
            return stockByArticle.getOrDefault(article, 0);
        }

        @Override
        public void decrementer(Article article, int quantity) {
            stockByArticle.put(article, quantiteDisponible(article) - quantity);
        }
    }

    private static final class InMemoryCommandeRepository implements com.it.exalt.belair.domain.CommandeRepository {
        private com.it.exalt.belair.domain.Commande savedCommande;

        @Override
        public com.it.exalt.belair.domain.Commande save(com.it.exalt.belair.domain.Commande commande) {
            savedCommande = commande;
            return commande;
        }

        @Override
        public java.util.Optional<com.it.exalt.belair.domain.Commande> findById(java.util.UUID id) {
            return java.util.Optional.ofNullable(savedCommande)
                    .filter(commande -> commande.id().equals(id));
        }

        @Override
        public com.it.exalt.belair.domain.Commande updateStatus(java.util.UUID id, com.it.exalt.belair.domain.CommandeStatus status) {
            throw new UnsupportedOperationException("Not needed by this application test");
        }

        @Override
        public List<com.it.exalt.belair.domain.Commande> findByFestivalierIdAndStatus(String festivalierId, com.it.exalt.belair.domain.CommandeStatus status) {
            throw new UnsupportedOperationException("Not needed by this application test");
        }
    }
}
