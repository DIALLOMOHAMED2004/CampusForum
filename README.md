# CampusForum

Application Android native (Java + SQLite) pour un forum académique local, pensée pour fonctionner **hors ligne**.

## Objectif

CampusForum permet aux étudiants et enseignants de :
- créer des comptes locaux,
- publier des sujets académiques,
- répondre aux sujets,
- organiser les discussions par catégories,
- suivre l’activité via un tableau de bord.

## Fonctionnalités implémentées

### Authentification locale
- Inscription (`RegisterActivity`)
- Connexion (`LoginActivity`)
- Récupération du mot de passe par question/réponse de sécurité (`ForgotPasswordActivity`)
- Gestion de session via `SharedPreferences` (`SessionManager`)

### Forum
- Liste des sujets avec recherche texte et filtres par catégorie (`HomeFragment`)
- Création de sujet (`CreateTopicActivity`)
- Détail d’un sujet + réponses (`TopicDetailActivity`)
- Édition / suppression de sujet (selon permissions)
- Ajout, édition et suppression de réponses (selon permissions)

### Profil
- Informations utilisateur
- Bio modifiable
- Statistiques personnelles (nombre de sujets/réponses)
- Historique des sujets récents
- Déconnexion

### Dashboard & Administration
- Dashboard d’activité (sujets, réponses, catégories, membres, activités récentes)
- Espace admin conditionné au rôle `ADMIN`
- Gestion des catégories (ajout / suppression)

## Stack technique

- **Langage** : Java 11
- **Plateforme** : Android
- **Base de données** : SQLite (`SQLiteOpenHelper`)
- **UI** : AndroidX + Material Components + RecyclerView
- **Build** : Gradle Kotlin DSL

Versions principales (cf. `gradle/libs.versions.toml`) :
- AGP `9.2.1`
- AppCompat `1.6.1`
- Material `1.10.0`
- Activity KTX `1.8.0`
- ConstraintLayout `2.1.4`

## Architecture du projet

Le module principal est `app/` avec une organisation par couches :
- `ui/` : Activities et Fragments
- `repository/` : règles métier + permissions
- `dao/` : accès SQLite
- `database/` : schéma, contrat et helper
- `model/` : entités (`User`, `Category`, `Topic`, `Reply`)
- `adapter/` : adaptateurs RecyclerView
- `utils/` : utilitaires (`PasswordUtils`, `DateUtils`, `SessionManager`)

## Base de données

Base locale : `campusforum.db`.

Tables principales :
- `users`
- `categories`
- `topics`
- `replies`

Le schéma est défini dans :
- `/home/runner/work/CampusForum/CampusForum/app/src/main/java/com/example/campusforum/database/DatabaseContract.java`
- `/home/runner/work/CampusForum/CampusForum/app/src/main/java/com/example/campusforum/database/DatabaseHelper.java`

À la création de la base, des catégories par défaut et un compte administrateur sont insérés automatiquement.

## Compte administrateur par défaut

Créé automatiquement si absent :
- **Email** : `admin@campusforum.local`
- **Mot de passe** : `Admin123`

> Recommandation : changer ce mot de passe dès les premiers tests.

## Pré-requis

- Android Studio récent
- SDK Android correspondant à la configuration du projet (compile/target SDK 36)
- JDK 11

## Lancer le projet

1. Ouvrir le dossier racine dans Android Studio  
   `/home/runner/work/CampusForum/CampusForum`
2. Synchroniser Gradle
3. Lancer l’application sur émulateur ou appareil Android

## Commandes utiles (depuis la racine)

```bash
./gradlew assembleDebug
./gradlew test
./gradlew connectedAndroidTest
```

## Documentation complémentaire

Le dossier `docs/` contient la documentation produit/design/handoff, notamment :
- `docs/paraflow/README_CAMPUSFORUM.md`
- `docs/paraflow/handoff_technique_android.md`
- `docs/PHASE1_CADRAGE_TECHNIQUE.md`

## État du projet

Projet fonctionnel orienté MVP, avec architecture claire et base locale robuste, prêt pour des améliorations futures (synchronisation, pièces jointes, notifications, etc.).
