# Tableau de Bord
Page affichant les statistiques d'activité de la communauté

## Barre Supérieure
- Titre "Tableau de Bord"
- Icône rafraîchir

## Section Statistiques Principales
- Grille de 4 cartes statistiques (2x2)

### Carte Sujets Actifs
- Icône sujet/document
- Nombre total de sujets (is_deleted = 0)
- Label "Sujets Actifs"
- Couleur primaire

### Carte Réponses
- Icône commentaire
- Nombre total de réponses (is_deleted = 0)
- Label "Réponses"
- Couleur secondaire

### Carte Catégories
- Icône tag/folder
- Nombre de catégories actives
- Label "Catégories"
- Couleur tertiaire

### Carte Membres (Admin uniquement)
- Icône group/people
- Nombre d'utilisateurs (is_active = 1)
- Label "Membres Actifs"
- Couleur accent
- Visible uniquement si role = ADMIN

## Section Activité Récente
- Titre "Dernières Activités"
- Liste des 5 derniers sujets créés
- Chaque item affiche :
  - Icône indicateur nouveau
  - Titre du sujet (1 ligne, ellipsis)
  - Auteur et date (format relatif)
  - Badge catégorie
- Clic sur item → TopicDetailActivity

## Section Catégories Populaires (Optionnel)
- Titre "Catégories les Plus Actives"
- Liste des 5 catégories avec le plus de sujets
- Chaque item affiche :
  - Badge couleur catégorie
  - Nom de la catégorie
  - Nombre de sujets
- Clic sur item → Accueil filtré par catégorie

## État de Chargement
- Shimmer effect pendant récupération données SQLite
- Message erreur si échec

## Action de Rafraîchissement
- Pull-to-refresh
- Bouton icône rafraîchir en header
- Recalcul de toutes les statistiques

## TabBar Navigation
- Accueil
- Tableau de Bord (actif)
- Profil
- [Admin] (si admin)
