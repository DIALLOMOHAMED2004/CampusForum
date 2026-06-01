# Splash
Écran de démarrage qui vérifie la session locale et redirige vers l'écran approprié

## Logo et Branding
- Logo de l'application CampusForum centré
- Nom de l'application "CampusForum"
- Slogan "Forum Académique Local"

## Chargement
- Indicateur de progression
- Vérification de la session utilisateur via SharedPreferences
- Initialisation de la base de données SQLite

## Navigation Automatique
- Si session valide : redirection vers Accueil
- Si pas de session : redirection vers Connexion
- Timeout maximum : 2 secondes
