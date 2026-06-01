# Mot de Passe Oublié
Page de récupération locale du mot de passe via question de sécurité

## En-tête
- Bouton retour → LoginActivity
- Titre "Récupération de Compte"
- Sous-titre "Répondez à votre question de sécurité"

## Étape 1 : Identification
- Champ Email (email, obligatoire)
- Bouton "Continuer"
- Vérification existence email dans SQLite
- Message erreur si email inexistant

## Étape 2 : Question de Sécurité
- Affichage de la question associée au compte
- Champ Réponse (texte, obligatoire, min 3 caractères)
- Bouton "Vérifier"
- Hachage et comparaison avec SQLite
- Message erreur si réponse incorrecte

## Étape 3 : Nouveau Mot de Passe
- Champ Nouveau Mot de Passe (password, obligatoire, min 6 caractères)
- Champ Confirmation (password, obligatoire, correspondance)
- Indicateur de force du mot de passe
- Bouton "Réinitialiser"
- Hachage et mise à jour SQLite
- Message succès "Mot de passe réinitialisé avec succès"
- Redirection automatique vers LoginActivity après 2 secondes

## Messages et États
- Indicateurs de progression entre les étapes
- Messages d'erreur contextuels clairs
- Confirmation visuelle de succès
