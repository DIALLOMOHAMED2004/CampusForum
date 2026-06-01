# Connexion
Page de connexion permettant aux utilisateurs existants d'accéder à leur compte

## En-tête
- Logo CampusForum
- Titre "Bienvenue"
- Sous-titre "Connectez-vous à votre compte"

## Formulaire de Connexion
- Champ Email (type email, obligatoire, validation format email)
- Champ Mot de Passe (type password, obligatoire, masqué, icône afficher/masquer)
- Bouton "Se Connecter" (pleine largeur, primaire)
- Messages d'erreur contextuels : "Email ou mot de passe incorrect", "Champs obligatoires"

## Actions Secondaires
- Lien "Mot de passe oublié ?" → ForgotPasswordActivity
- Séparateur avec texte "Nouveau sur CampusForum ?"
- Bouton "Créer un Compte" (outline) → RegisterActivity

## États et Validation
- Validation en temps réel des champs
- Désactivation du bouton pendant le chargement
- Indicateur de progression pendant la vérification
- Hachage et comparaison avec SQLite
- Création de session SharedPreferences si succès
