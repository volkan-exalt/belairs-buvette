package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.Article;
import com.it.exalt.belair.domain.Commande;
import com.it.exalt.belair.domain.CommandeStatus;
import com.it.exalt.belair.domain.LigneCommande;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandeRepositoryImplTest {

    @Test
    void shouldSaveAndFindNewCommandeById() {
        CommandeRepositoryImpl repository = new CommandeRepositoryImpl();
        Commande commande = new Commande(
                UUID.randomUUID(),
                "festivalier-42",
                CommandeStatus.EN_ATTENTE,
                List.of(
                        new LigneCommande(new Article("Mojito"), 2),
                        new LigneCommande(new Article("Eau plate"), 1)
                )
        );

        repository.save(commande);
        Commande found = repository.findById(commande.id()).orElseThrow();

        assertEquals(CommandeStatus.EN_ATTENTE, found.status());
        assertEquals(commande.lignes(), found.lignes());
        assertEquals(2, found.lignes().size());
    }

    @Test
    void shouldUpdateCommandeStatus() {
        CommandeRepositoryImpl repository = new CommandeRepositoryImpl();
        Commande commande = new Commande(
                UUID.randomUUID(),
                "festivalier-42",
                CommandeStatus.EN_ATTENTE,
                List.of(new LigneCommande(new Article("Mojito"), 2))
        );
        repository.save(commande);

        repository.updateStatus(commande.id(), CommandeStatus.PRETE);

        assertEquals(CommandeStatus.PRETE, repository.findById(commande.id()).orElseThrow().status());
    }

    @Test
    void shouldFindPendingCommandesForFestivalier() {
        CommandeRepositoryImpl repository = new CommandeRepositoryImpl();
        repository.save(new Commande(
                UUID.randomUUID(),
                "festivalier-42",
                CommandeStatus.EN_ATTENTE,
                List.of(new LigneCommande(new Article("Mojito"), 1))
        ));
        repository.save(new Commande(
                UUID.randomUUID(),
                "festivalier-42",
                CommandeStatus.EN_ATTENTE,
                List.of(new LigneCommande(new Article("Eau plate"), 1))
        ));
        repository.save(new Commande(
                UUID.randomUUID(),
                "festivalier-42",
                CommandeStatus.PRETE,
                List.of(new LigneCommande(new Article("Chips"), 1))
        ));

        List<Commande> pendingCommandes = repository.findByFestivalierIdAndStatus("festivalier-42", CommandeStatus.EN_ATTENTE);

        assertEquals(2, pendingCommandes.size());
    }
}
