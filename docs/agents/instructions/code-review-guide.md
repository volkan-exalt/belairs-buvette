# Code Review Guide

Ces consignes s'appliquent lors d'une revue de code.

## Priorites

- Chercher d'abord les bugs, regressions comportementales, oublis de tests et violations de regles metier.
- Verifier que les changements respectent la separation `domain`, `application`, `infrastructure`.
- Signaler les risques concrets avec le fichier et la ligne concernes quand c'est possible.
- Eviter les remarques de style mineures si elles ne changent pas la comprehension ou la maintenabilite.

## Checklist

- Le domaine reste independant de l'infrastructure.
- Les validations metier utilisent `DomainValidationException`.
- Les collections exposees ne peuvent pas etre modifiees depuis l'exterieur.
- Les tests couvrent les chemins nominaux et les erreurs importantes.
- Les changements ne touchent pas des fichiers generes ou hors sujet.

## Format attendu

- Presenter les findings en premier, du plus severe au moins severe.
- Inclure les questions ouvertes ensuite.
- Ajouter un court resume seulement apres les points importants.
- Si aucun probleme n'est trouve, le dire clairement et mentionner les risques residuels ou tests non executes.
