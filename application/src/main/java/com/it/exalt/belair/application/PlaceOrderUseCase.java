package com.it.exalt.belair.application;

import com.it.exalt.belair.domain.Article;
import com.it.exalt.belair.domain.Commande;
import com.it.exalt.belair.domain.FestivalGoer;
import com.it.exalt.belair.domain.LigneCommande;
import com.it.exalt.belair.domain.OrderLine;
import com.it.exalt.belair.domain.PasserCommandeUseCase;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PlaceOrderUseCase implements CreerCommandeUseCase {
    private final PasserCommandeUseCase passerCommandeUseCase;

    public PlaceOrderUseCase() {
        this.passerCommandeUseCase = null;
    }

    public PlaceOrderUseCase(PasserCommandeUseCase passerCommandeUseCase) {
        this.passerCommandeUseCase = Objects.requireNonNull(passerCommandeUseCase, "passerCommandeUseCase must not be null");
    }

    public OrderResult placeOrder(FestivalGoer festivalGoer, List<OrderLine> orderLines) {
        festivalGoer.placeOrder(orderLines);
        return new OrderResult("EN_ATTENTE", UUID.randomUUID());
    }

    @Override
    public CreerCommandeResult creerCommande(CreerCommandeCommand command) {
        if (passerCommandeUseCase == null) {
            throw new IllegalStateException("passerCommandeUseCase is required to create commandes from API commands");
        }
        List<LigneCommande> lignes = command.articles().stream()
                .map(article -> new LigneCommande(new Article(article.nom()), article.quantite()))
                .toList();
        Commande commande = passerCommandeUseCase.passerCommande(command.festivalierId(), lignes);
        return new CreerCommandeResult(commande.id(), commande.status().name());
    }
}
