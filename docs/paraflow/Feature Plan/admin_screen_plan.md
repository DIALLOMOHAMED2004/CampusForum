# Administration
Page de gestion réservée aux administrateurs (role = ADMIN uniquement)

## Barre Supérieure
- Bouton retour → Accueil
- Titre "Administration"
- Badge "ADMIN"

## Vérification d'Accès
- Contrôle role = ADMIN au chargement
- Si non admin : Toast "Accès refusé" et redirection automatique

## Section Statistiques Globales
- Grille 4 cartes (2x2)

### Carte Utilisateurs Totaux
- Icône people
- COUNT(*) de la table users
- Label "Utilisateurs Inscrits"

### Carte Sujets Supprimés
- Icône delete
- COUNT(is_deleted = 1) de la table topics
- Label "Sujets Supprimés"

### Carte Réponses Supprimées
- Icône remove
- COUNT(is_deleted = 1) de la table replies
- Label "Réponses Supprimées"

### Carte Catégories Actives
- Icône category
- COUNT(*) de la table categories
- Label "Catégories"

## Section Gestion des Catégories
- Titre "Catégories"
- Bouton "+ Nouvelle Catégorie"
- RecyclerView des catégories existantes
- Chaque item affiche :
  - Badge couleur catégorie
  - Nom de la catégorie
  - Nombre de sujets associés
  - Boutons actions :
    - Modifier (icône edit)
    - Supprimer (icône delete, confirmation)
- Clic "+ Nouvelle" → Dialog création catégorie
  - Champ Nom (obligatoire, unique)
  - Champ Description (optionnel)
  - Sélecteur couleur (7 couleurs prédéfinies)

## Section Liste des Utilisateurs
- Titre "Utilisateurs"
- RecyclerView des utilisateurs
- Chaque item affiche :
  - Avatar/Icône
  - Nom d'utilisateur
  - Email
  - Badge rôle (USER/ADMIN)
  - Nombre de sujets créés
  - Nombre de réponses
  - Date d'inscription
- Filtres :
  - Tous
  - Administrateurs
  - Utilisateurs actifs
  - Nouveaux (< 7 jours)

## Section Dernières Activités
- Titre "Dernières Activités"
- Liste des 20 dernières actions :
  - Nouveaux utilisateurs inscrits
  - Nouveaux sujets créés
  - Sujets supprimés (avec indication auteur suppression)
  - Réponses supprimées
- Chaque item avec date/heure, auteur, action

## Actions de Modération
- Bouton "Voir Contenus Supprimés"
  - Liste sujets/réponses avec is_deleted = 1
  - Possibilité de restauration (is_deleted = 0)

## TabBar Navigation
- Accueil
- Tableau de Bord
- Profil
- Admin (actif, visible uniquement si ADMIN)
