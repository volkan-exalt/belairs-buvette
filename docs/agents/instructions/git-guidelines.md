# Git Guidelines

Ces consignes s'appliquent lors des commits, branches, PRs et analyses Git.

## Hygiene

- Verifier `git status --short` avant de preparer un commit.
- Ne jamais supprimer ou revert des changements non compris sans demande explicite.
- Ignorer les fichiers de build et sorties generees quand ils ne font pas partie de la demande.
- Eviter de committer `bin/`, `build/`, `.gradle/` ou fichiers IDE locaux.

## Commits

- Faire des commits petits et coherents.
- Utiliser un message clair au present, par exemple :
  - `Add in-memory infrastructure adapters`
  - `Split tests by production class`
  - `Document agent development guidelines`
- Mentionner les tests lances dans la description de PR ou le message final si utile.

## Branches et PRs

- Nommer les branches avec une intention courte, par exemple `docs/agent-guidelines` ou `test/split-domain-infrastructure`.
- Dans une PR, decrire :
  - le contexte,
  - les changements principaux,
  - les tests executes,
  - les limites connues.

## Securite

- Ne pas inclure de secrets, tokens ou informations personnelles dans les commits.
- Ne pas reecrire l'historique partage sans instruction claire.
