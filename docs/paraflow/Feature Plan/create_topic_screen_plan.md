# Créer un Sujet
Page de création d'un nouveau sujet de discussion

## Barre Supérieure
- Bouton retour → Accueil (confirmation si modifications non sauvegardées)
- Titre "Nouveau Sujet"
- Bouton "Publier" (désactivé si formulaire invalide)

## Formulaire de Création
- Champ Titre (texte, obligatoire, min 5 caractères, max 100 caractères)
  - Placeholder "Titre de votre sujet..."
  - Compteur caractères "0/100"
  - Message erreur contextuel si < 5 caractères

- Sélecteur de Catégorie (dropdown/spinner, obligatoire)
  - Placeholder "Sélectionnez une catégorie"
  - Liste des catégories actives depuis SQLite
  - Affichage badge couleur catégorie sélectionnée

- Champ Contenu (textarea multiligne, obligatoire, min 10 caractères, max 2000 caractères)
  - Placeholder "Décrivez votre question ou partagez votre information..."
  - Compteur caractères "0/2000"
  - Expansion automatique jusqu'à hauteur maximale

## Validation en Temps Réel
- Indicateurs visuels (rouge/vert) pour champs obligatoires
- Messages d'aide contextuels
- Activation bouton "Publier" uniquement si formulaire valide

## Actions
- Bouton "Publier" (pleine largeur, primaire)
  - Désactivé pendant soumission
  - Indicateur de progression
  - Insertion SQLite avec author_id, created_at
  - Message succès "Sujet créé avec succès"
  - Redirection vers TopicDetailActivity du nouveau sujet

- Bouton "Annuler" (secondaire)
  - Dialog confirmation si modifications présentes
  - Retour vers Accueil

## États et Messages
- État de chargement des catégories
- Messages d'erreur clairs (titre trop court, catégorie non sélectionnée, contenu vide)
- Confirmation visuelle avant publication
