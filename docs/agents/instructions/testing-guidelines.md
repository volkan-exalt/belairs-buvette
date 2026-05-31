# Testing Guidelines

Ces consignes s'appliquent lors de l'ecriture, modification ou revue de tests.

## Organisation

- Creer un fichier de test par classe de production quand c'est pertinent.
- Nommer les tests avec le suffixe `Test`.
- Placer les tests dans le meme package logique que la classe testee.
- Garder les tests du domaine dans `domain/src/test/java`.
- Garder les tests d'infrastructure dans `infrastructure/src/test/java`.

## Style des tests

- Utiliser JUnit Jupiter.
- Ecrire des tests lisibles avec une intention claire par methode.
- Couvrir le comportement nominal et les validations importantes.
- Utiliser `assertThrows` pour les regles d'erreur.
- Eviter les tests trop couples aux details internes si le comportement public suffit.

## Domaine

- Tester les regles metier sans dependance technique.
- Verifier les transitions de statut, les couts de jetons, les remboursements et les validations.
- Les tests doivent refleter `FEATURES_fr.md`.

## Infrastructure

- Tester les adaptateurs via leurs effets observables : sauvegarde, recherche, suppression, notifications envoyees.
- Pour le temps, utiliser `Clock.fixed(...)` ou une horloge controlable.
- Ne pas utiliser de sommeil reel, reseau ou base de donnees pour les tests unitaires.

## Commandes

- Pour les changements de domaine : `./gradlew :domain:test`
- Pour les changements d'infrastructure : `./gradlew :infrastructure:test`
- Pour une validation complete : `./gradlew test`
- Sur Windows, utiliser `gradlew.bat`.
