# AGENTS.md Maintenance

Ces consignes s'appliquent lors des modifications de `AGENTS.md` ou des guidelines d'agents.

## Objectif

Le fichier `AGENTS.md` racine doit rester court. Il sert a router l'agent vers les fichiers specialises, pas a contenir toute la documentation du projet.

## Principes

- Garder `AGENTS.md` oriente contexte global et chargement conditionnel.
- Placer les details durables dans `docs/agents/instructions/`.
- Utiliser des references conditionnelles claires : chaque fichier doit indiquer quand il doit etre charge.
- Eviter les doublons entre `AGENTS.md` et les fichiers de guidelines.
- Ne pas ajouter une guideline si une guideline existante couvre deja le besoin.

## Ajout ou modification de guidelines

- Creer les nouveaux fichiers dans `docs/agents/instructions/`.
- Nommer les fichiers en kebab-case.
- Ajouter ou mettre a jour la reference correspondante dans la section `## Guidelines specialisees` de `AGENTS.md`.
- Preciser le contexte de chargement en une phrase concrete.
- Relire et corriger les guidelines generees par l'agent avant de les integrer.

## Verification

- Verifier que les chemins references existent.
- Verifier qu'au moins trois fichiers de guidelines sont references pour le lab.
- Utiliser la vue de debug du chat Copilot pour confirmer quels fichiers `AGENTS.md` et quelles instructions ont ete effectivement charges.
- Tester le comportement attendu avec un prompt de validation adapte a la guideline modifiee.
