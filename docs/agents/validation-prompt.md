# Prompt de validation des guidelines

Prompt utilise pour verifier le cablage conditionnel :

```text
Tu dois modifier une classe Java du domaine pour ajouter une petite validation metier.
Charge uniquement les guidelines pertinentes pour une tache de developpement.
Explique brievement quelles guidelines tu appliques avant de proposer la modification.
```

Comportement attendu :

- L'agent charge ou applique `docs/agents/instructions/coding-guidelines.md`.
- L'agent ne charge pas `docs/agents/instructions/testing-guidelines.md`, sauf si la demande mentionne aussi l'ecriture ou la revue de tests.
- L'agent ne charge pas `docs/agents/instructions/code-review-guide.md`, sauf si la demande est une revue de code.
- L'agent respecte la separation des modules : les regles metier restent dans `domain`.
- L'agent utilise `DomainValidationException` pour une violation metier et garde le changement limite.

Verification effectuee :

- Les references conditionnelles sont presentes dans `AGENTS.md`.
- Les chemins references existent dans `docs/agents/instructions/`.
- Les regles de coding et testing sont separees afin que les tests ne soient pas charges pour une simple modification de code.
- La vue de debug du chat Copilot peut etre utilisee pour verifier les fichiers d'instructions effectivement charges.
