# Accueil
Page principale affichant la liste des sujets de discussion avec filtres et recherche

## Barre Supérieure
- Logo/Titre "CampusForum"
- Icône recherche → Barre de recherche expansible
- Icône filtrer → Menu de sélection catégorie

## Barre de Recherche (Optionnelle)
- Champ texte "Rechercher des sujets..."
- Icône recherche
- Recherche en temps réel (titre et contenu)
- Effacer recherche (icône X)

## Filtre par Catégorie
- Chip "Toutes" (sélectionné par défaut)
- Liste des catégories disponibles sous forme de chips horizontales scrollables
- Sélection d'une catégorie filtre la liste

## Liste des Sujets
- RecyclerView avec CardView pour chaque sujet
- Chaque carte contient :
  - Badge catégorie (couleur distinctive)
  - Titre du sujet (2 lignes max, ellipsis)
  - Aperçu contenu (1-2 lignes, ellipsis)
  - Nom de l'auteur avec icône utilisateur
  - Date de création/dernière modification (relative)
  - Nombre de réponses avec icône commentaire
  - Icône "Like" avec compte (si fonctionnalité activée)
- Clic sur carte → TopicDetailActivity
- Pull-to-refresh pour rafraîchir la liste
- Indicateur de chargement si liste vide

## État Vide
- Illustration "Aucun sujet"
- Message "Soyez le premier à créer un sujet"
- Bouton "Créer un Sujet"

## Bouton d'Action Flottant (FAB)
- Icône "+" en bas à droite
- Couleur primaire
- Clic → CreateTopicActivity
- Visible uniquement si utilisateur connecté

## TabBar Navigation
- Accueil (icône home, actif)
- Tableau de Bord (icône dashboard)
- Profil (icône person)
- [Admin] (icône settings, visible uniquement pour admin)
