# Inscription
Page d'inscription permettant la création d'un nouveau compte utilisateur

## En-tête
- Bouton retour → LoginActivity
- Titre "Créer un Compte"
- Sous-titre "Rejoignez la communauté académique"

## Formulaire d'Inscription
- Champ Nom d'Utilisateur (texte, obligatoire, 3-20 caractères, alphanumérique)
- Champ Email (email, obligatoire, unique dans SQLite, validation format)
- Champ Mot de Passe (password, obligatoire, min 6 caractères, indicateur de force)
- Champ Confirmation Mot de Passe (password, obligatoire, doit correspondre)
- Champ Question de Sécurité (sélection depuis liste prédéfinie)
  - "Nom de votre animal de compagnie ?"
  - "Ville de naissance ?"
  - "Nom de votre école primaire ?"
  - "Film préféré ?"
  - "Plat préféré ?"
- Champ Réponse de Sécurité (texte, obligatoire, min 3 caractères)

## Validation et Contraintes
- Vérification unicité email en temps réel
- Validation force mot de passe (minuscule, majuscule, chiffre)
- Correspondance mot de passe / confirmation
- Messages d'erreur contextuels

## Actions
- Bouton "Créer mon Compte" (pleine largeur, primaire)
- Hachage mot de passe et réponse sécurité
- Insertion SQLite avec rôle USER par défaut
- Création session automatique
- Redirection vers Accueil

## Pied de Page
- Texte "Déjà inscrit ?"
- Lien "Se Connecter" → LoginActivity
