# Profil
Page affichant les informations personnelles et les contributions de l'utilisateur

## Barre Supérieure
- Titre "Mon Profil"
- Menu actions (3 points)
  - Modifier Bio
  - Paramètres (optionnel)
  - Déconnexion

## En-tête Utilisateur
- Avatar large/Icône utilisateur (cercle)
- Nom d'utilisateur (grande taille)
- Badge rôle (USER ou ADMIN) avec couleur distinctive
- Email (petite taille, grisé)
- Date de création compte "Membre depuis [date]"

## Section Bio
- Titre "À propos"
- Texte bio (max 200 caractères, multiligne)
- Bouton "Modifier" → Dialog édition bio
- Si bio vide : "Ajoutez une bio pour vous présenter"

## Statistiques Personnelles
- Grille 2 cartes horizontales

### Carte Mes Sujets
- Icône document
- Nombre de sujets créés (author_id = current_user, is_deleted = 0)
- Label "Sujets Créés"

### Carte Mes Réponses
- Icône commentaire
- Nombre de réponses publiées (author_id = current_user, is_deleted = 0)
- Label "Réponses Publiées"

## Section Mes Contributions
- Titre "Mes Sujets Récents"
- Liste des 10 derniers sujets de l'utilisateur
- Chaque item affiche :
  - Titre du sujet (1-2 lignes, ellipsis)
  - Badge catégorie
  - Date de création (format relatif)
  - Nombre de réponses
- Clic sur item → TopicDetailActivity
- État vide : "Vous n'avez pas encore créé de sujet"

## Actions
- Bouton "Déconnexion" (pleine largeur, outline, rouge)
  - Dialog confirmation "Êtes-vous sûr de vouloir vous déconnecter ?"
  - Suppression session SharedPreferences
  - Redirection vers LoginActivity

## TabBar Navigation
- Accueil
- Tableau de Bord
- Profil (actif)
- [Admin] (si admin)
