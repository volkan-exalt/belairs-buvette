package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasserCommandeUseCaseTest {

    @Test
    void shouldCreatePendingCommandeAndDecrementStockWhenStockIsSufficient() {
        InMemoryStockRepository stockRepository = new InMemoryStockRepository();
        stockRepository.add("Mojito", 10);
        PasserCommandeUseCase useCase = new PasserCommandeUseCase(stockRepository);

        Commande commande = useCase.passerCommande(List.of(new LigneCommande(new Article("Mojito"), 2)));

        assertEquals(CommandeStatus.EN_ATTENTE, commande.status());
        assertEquals(8, stockRepository.quantiteDisponible(new Article("Mojito")));
    }

    @Test
    void shouldRejectCommandeAndKeepStockUnchangedWhenStockIsInsufficient() {
        InMemoryStockRepository stockRepository = new InMemoryStockRepository();
        stockRepository.add("Mojito", 1);
        PasserCommandeUseCase useCase = new PasserCommandeUseCase(stockRepository);

        StockInsuffisantException exception = assertThrows(
                StockInsuffisantException.class,
                () -> useCase.passerCommande(List.of(new LigneCommande(new Article("Mojito"), 2)))
        );

        assertEquals("STOCK_INSUFFISANT", exception.code());
        assertEquals(1, stockRepository.quantiteDisponible(new Article("Mojito")));
    }

    @Test
    void shouldRejectCommandeWhenArticleDoesNotExistInCatalog() {
        InMemoryStockRepository stockRepository = new InMemoryStockRepository();
        PasserCommandeUseCase useCase = new PasserCommandeUseCase(stockRepository);

        ArticleInconnuException exception = assertThrows(
                ArticleInconnuException.class,
                () -> useCase.passerCommande(List.of(new LigneCommande(new Article("Champagne"), 1)))
        );

        assertEquals("ARTICLE_INCONNU", exception.code());
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
}
