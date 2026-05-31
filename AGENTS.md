# AGENTS.md

Instructions racine pour les agents travaillant sur le projet `belairs-buvette`.

## Contexte projet

Ce depot est un projet Java 21 organise en modules Gradle :

- `domain/` contient la logique metier et le modele de domaine.
- `application/` est reserve aux cas d'usage applicatifs.
- `infrastructure/` contient les adaptateurs, la persistance et les integrations externes.

Les fonctionnalites attendues sont decrites dans `FEATURES_fr.md`. Le `README_fr.md` decrit la structure du projet et les commandes principales.

## Offloading du contexte

Ne charge pas toutes les guidelines par defaut. Le contexte est limite : charge uniquement les fichiers specialises utiles a la tache en cours.

## Guidelines specialisees

- Pour tout travail sur du code Java, charge et applique les regles de [coding-guidelines.md](docs/agents/instructions/coding-guidelines.md).
- Lors de l'ecriture, la modification ou la revue de tests, charge [testing-guidelines.md](docs/agents/instructions/testing-guidelines.md).
- Lors de commits, branches, PRs ou analyse Git, charge [git-guidelines.md](docs/agents/instructions/git-guidelines.md).
- Lors d'une revue de code, charge [code-review-guide.md](docs/agents/instructions/code-review-guide.md).
- Lors de la redaction ou modification de documentation Markdown, charge [documentation-guidelines.md](docs/agents/instructions/documentation-guidelines.md).
- Lors de toute modification de `AGENTS.md` ou des guidelines d'agents, charge [agents-md-maintenance.md](docs/agents/instructions/agents-md-maintenance.md).

## Workflow par defaut

- Lire le contexte existant avant de modifier le code.
- Respecter la separation des modules : le domaine ne depend pas de l'infrastructure.
- Preferer des changements petits, lisibles et testes.
- Lancer les tests Gradle pertinents apres une modification de code.
- Ne pas modifier les fichiers generes ou les sorties de build sauf demande explicite.
