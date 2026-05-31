# Coding Guidelines

Ces consignes s'appliquent a chaque tache de developpement Java dans ce depot.

## Stack et structure

- Utiliser Java 21 et les conventions Gradle deja presentes.
- Respecter l'architecture multi-modules :
  - `domain` porte les regles metier pures.
  - `application` orchestre les cas d'usage si une couche applicative est necessaire.
  - `infrastructure` contient les adaptateurs techniques, repositories, notifications et integrations.
- Ne pas introduire de dependance du module `domain` vers `application` ou `infrastructure`.
- Garder les noms de packages sous `com.it.exalt.belair`.

## Style Java

- Favoriser des classes petites avec une responsabilite claire.
- Utiliser des types immuables quand c'est naturel, comme les records ou les champs `final`.
- Valider les entrees publiques avec des exceptions explicites.
- Utiliser `DomainValidationException` pour les violations de regles metier.
- Utiliser `Objects.requireNonNull(...)` pour les dependances ou arguments techniques obligatoires.
- Retourner des vues non modifiables ou des copies defensives pour les collections exposees.

## Domaine

- Les regles de `FEATURES_fr.md` doivent rester dans le domaine quand elles concernent le metier.
- Eviter la logique de persistance, de notification ou d'horloge systeme directement dans le domaine.
- Les entites et value objects doivent rester faciles a tester sans framework externe.

## Infrastructure

- Les adaptateurs en memoire sont acceptables pour ce lab.
- Les repositories doivent rester simples et deterministes.
- Les services d'infrastructure peuvent dependre du domaine, mais ne doivent pas changer ses invariants.

## Lisibilite

- Preferer les noms explicites aux abreviations.
- Ajouter des commentaires seulement quand une intention n'est pas evidente dans le code.
- Ne pas faire de refactor global sans lien direct avec la demande.
