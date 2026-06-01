# Détail du Sujet
Page affichant le contenu complet d'un sujet avec toutes ses réponses

## Barre Supérieure
- Bouton retour → Accueil
- Titre "Sujet"
- Menu actions (3 points verticaux, visible si auteur ou admin)
  - Modifier → EditTopicActivity
  - Supprimer (confirmation dialog)

## En-tête du Sujet
- Badge catégorie
- Titre du sujet (taille grande, multilignes)
- Informations auteur :
  - Avatar/Icône utilisateur
  - Nom de l'auteur
  - Rôle (si ADMIN)
  - Date de création
  - Icône "Modifié le" si updated_at différent
- Icône "Like" avec compte (optionnel)
- Bouton "J'aime" si utilisateur connecté

## Contenu du Sujet
- Corps du texte (multiligne, formatage conservé)
- Espacement confortable pour lecture

## Séparateur
- Ligne horizontale
- Badge "Réponses (nombre)"

## Liste des Réponses
- RecyclerView des réponses chronologiques
- Chaque réponse contient :
  - Avatar/Icône répondeur
  - Nom du répondeur
  - Date de réponse (relative)
  - Contenu de la réponse (multiligne)
  - Menu actions (3 points, si auteur ou admin)
    - Modifier
    - Supprimer (confirmation)
- Séparateurs entre réponses

## État Sans Réponse
- Illustration "Aucune réponse"
- Message "Soyez le premier à répondre"

## Zone d'Ajout de Réponse
- Avatar utilisateur actuel
- Champ texte multiligne "Écrire une réponse..."
- Compteur caractères (optionnel)
- Bouton "Envoyer" (visible si texte non vide)
- Désactivé si utilisateur non connecté avec message "Connectez-vous pour répondre"

## TabBar Navigation
- Accueil
- Tableau de Bord
- Profil
- [Admin] (si admin)
