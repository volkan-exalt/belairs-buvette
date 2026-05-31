package com.it.exalt.belair.application.api;

import com.it.exalt.belair.application.ArticleCommande;
import com.it.exalt.belair.application.CreerCommandeCommand;
import com.it.exalt.belair.application.CreerCommandeResult;
import com.it.exalt.belair.application.CreerCommandeUseCase;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandesControllerTest {

    @Test
    void shouldReturnCreatedResponseWhenCommandeIsCreated() {
        UUID commandeId = UUID.randomUUID();
        FakeCreerCommandeUseCase useCase = new FakeCreerCommandeUseCase(
                new CreerCommandeResult(commandeId, "EN_ATTENTE")
        );
        CommandesController controller = new CommandesController(useCase);
        CreerCommandeRequest request = new CreerCommandeRequest(
                "festivalier-1",
                List.of(new ArticleCommandeRequest("Mojito", 1))
        );

        ApiResponse<CreerCommandeResponse> response = controller.postCommandes(request);

        assertEquals(201, response.statusCode());
        assertEquals(commandeId, response.body().commandeId());
        assertEquals("EN_ATTENTE", response.body().status());
    }

    @Test
    void shouldDelegateFestivalierAndArticlesToUseCase() {
        FakeCreerCommandeUseCase useCase = new FakeCreerCommandeUseCase(
                new CreerCommandeResult(UUID.randomUUID(), "EN_ATTENTE")
        );
        CommandesController controller = new CommandesController(useCase);
        CreerCommandeRequest request = new CreerCommandeRequest(
                "festivalier-1",
                List.of(new ArticleCommandeRequest("Mojito", 1))
        );

        controller.postCommandes(request);

        assertNotNull(useCase.receivedCommand);
        assertEquals("festivalier-1", useCase.receivedCommand.festivalierId());
        assertEquals(List.of(new ArticleCommande("Mojito", 1)), useCase.receivedCommand.articles());
    }

    @Test
    void shouldRejectCommandeWithoutArticles() {
        FakeCreerCommandeUseCase useCase = new FakeCreerCommandeUseCase(
                new CreerCommandeResult(UUID.randomUUID(), "EN_ATTENTE")
        );
        CommandesController controller = new CommandesController(useCase);
        CreerCommandeRequest request = new CreerCommandeRequest("festivalier-1", List.of());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> controller.postCommandes(request)
        );

        assertEquals("articles must not be empty", exception.getMessage());
        assertFalse(useCase.called);
    }

    private static final class FakeCreerCommandeUseCase implements CreerCommandeUseCase {
        private final CreerCommandeResult result;
        private CreerCommandeCommand receivedCommand;
        private boolean called;

        private FakeCreerCommandeUseCase(CreerCommandeResult result) {
            this.result = result;
        }

        @Override
        public CreerCommandeResult creerCommande(CreerCommandeCommand command) {
            called = true;
            receivedCommand = command;
            return result;
        }
    }
}
