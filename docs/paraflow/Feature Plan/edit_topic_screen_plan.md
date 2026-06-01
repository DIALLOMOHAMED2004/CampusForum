# Modifier le Sujet
Page de modification d'un sujet existant (accessible uniquement à l'auteur ou admin)

## Barre Supérieure
- Bouton retour → TopicDetailActivity (confirmation si modifications non sauvegardées)
- Titre "Modifier le Sujet"
- Bouton "Sauvegarder" (désactivé si aucune modification)

## Formulaire de Modification
- Champ Titre (pré-rempli avec valeur actuelle)
  - Texte, obligatoire, min 5 caractères, max 100 caractères
  - Compteur caractères
  - Validation en temps réel

- Sélecteur de Catégorie (pré-sélectionné avec catégorie actuelle)
  - Dropdown avec toutes les catégories actives
  - Badge couleur catégorie sélectionnée

- Champ Contenu (pré-rempli avec contenu actuel)
  - Textarea multiligne, obligatoire, min 10 caractères, max 2000 caractères
  - Compteur caractères
  - Expansion automatique

## Indicateur de Modification
- Détection automatique des changements
- Affichage badge "Non sauvegardé" si modifications présentes
- Activation bouton "Sauvegarder" uniquement si changements et formulaire valide

## Actions
- Bouton "Sauvegarder" (pleine largeur, primaire)
  - Désactivé pendant soumission
  - Indicateur de progression
  - Mise à jour SQLite avec updated_at = now
  - Message succès "Sujet modifié avec succès"
  - Retour vers TopicDetailActivity

- Bouton "Annuler" (secondaire)
  - Dialog confirmation si modifications non sauvegardées
  - Retour vers TopicDetailActivity sans sauvegarder

## Vérifications de Sécurité
- Contrôle author_id = current_user_id OU role = ADMIN
- Si non autorisé : message erreur et redirection
- Si sujet supprimé (is_deleted = 1) : message et redirection

## États et Messages
- Chargement des données initiales
- Messages d'erreur contextuels
- Confirmation visuelle après sauvegarde
