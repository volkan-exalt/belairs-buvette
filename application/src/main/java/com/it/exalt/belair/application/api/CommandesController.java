package com.it.exalt.belair.application.api;

import com.it.exalt.belair.application.ArticleCommande;
import com.it.exalt.belair.application.CreerCommandeCommand;
import com.it.exalt.belair.application.CreerCommandeResult;
import com.it.exalt.belair.application.CreerCommandeUseCase;

import java.util.List;
import java.util.Objects;

public class CommandesController {
    private final CreerCommandeUseCase creerCommandeUseCase;

    public CommandesController(CreerCommandeUseCase creerCommandeUseCase) {
        this.creerCommandeUseCase = Objects.requireNonNull(creerCommandeUseCase, "creerCommandeUseCase must not be null");
    }

    public ApiResponse<CreerCommandeResponse> postCommandes(CreerCommandeRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        if (request.articles().isEmpty()) {
            throw new IllegalArgumentException("articles must not be empty");
        }

        List<ArticleCommande> articles = request.articles().stream()
                .map(article -> new ArticleCommande(article.nom(), article.quantite()))
                .toList();
        CreerCommandeResult result = creerCommandeUseCase.creerCommande(
                new CreerCommandeCommand(request.festivalierId(), articles)
        );

        return new ApiResponse<>(
                201,
                new CreerCommandeResponse(result.commandeId(), result.status())
        );
    }
}
