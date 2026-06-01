# PRD — CampusForum Application Mobile Android

## 1) Background

Les étudiants et enseignants universitaires ont besoin d'échanger des informations académiques, poser des questions et collaborer efficacement au quotidien. Cependant, les solutions existantes (forums web, réseaux sociaux, plateformes cloud) nécessitent une connexion Internet permanente et offrent des fonctionnalités inadaptées au contexte académique. Cette itération adresse le besoin fondamental exprimé dans le Product Charter d'un "espace de dialogue académique mobile, local et autonome", en se concentrant sur les scénarios principaux d'Amina (étudiante active) et du Dr. Rodriguez (modérateur académique). C'est le bon moment pour développer cette solution car les infrastructures réseau restent instables dans de nombreux campus universitaires, créant une frustration quotidienne pour les communautés académiques qui dépendent d'outils cloud inaccessibles sans connexion.

## 2) Objectifs & Résultats Attendus

- **Autonomie locale:** Les utilisateurs créent des comptes, se connectent et participent aux discussions sans jamais dépendre d'une connexion Internet, leur permettant d'utiliser l'application n'importe où sur le campus, même dans les zones sans couverture réseau
- **Organisation académique:** Les discussions sont structurées par catégories thématiques, permettant aux étudiants de retrouver rapidement les informations pertinentes à leurs cours et projets sans perdre de temps dans des contenus non pertinents
- **Engagement communautaire:** Les utilisateurs consultent un tableau de bord d'activité qui visualise clairement la vitalité de la communauté et leur propre contribution, les encourageant à participer davantage et à construire une base de connaissances collective
- **Modération académique:** Les administrateurs maintiennent un environnement respectueux en gérant les catégories, modérant les contenus inappropriés et supervisant l'activité communautaire, garantissant que l'espace reste professionnel et académique

**Non-goals / Limites:**
- Pas de synchronisation multi-appareils ou cloud : les données restent strictement locales à l'appareil
- Pas de notifications push : l'application ne peut pas envoyer de notifications en temps réel
- Pas de fonctionnalités de messagerie directe : focus sur les discussions publiques uniquement
- Pas de système de gamification complexe : seules les fonctionnalités de likes simples sont envisagées en option
- Pas de support d'images ou fichiers attachés dans cette version MVP

## 3) Utilisateurs & Stories

- **Persona Principale:** Amina Diallo (Étudiante Active)
  - Story A: En tant qu'Amina, je veux créer un compte avec email, mot de passe et question de sécurité, afin de sécuriser mon accès personnel tout en conservant une méthode de récupération autonome sans dépendre d'un email externe
  - Story B: En tant qu'Amina, je veux créer un nouveau sujet avec titre, contenu et catégorie, afin de poser une question ou partager une information organisée que mes camarades pourront facilement trouver
  - Story C: En tant qu'Amina, je veux consulter la liste des sujets et filtrer par catégorie ou rechercher par mots-clés, afin de trouver rapidement les discussions pertinentes à mes cours sans parcourir manuellement tout le contenu
  - Story D: En tant qu'Amina, je veux lire le détail d'un sujet avec toutes les réponses associées, afin de comprendre le contexte complet de la discussion et bénéficier des contributions de la communauté
  - Story E: En tant qu'Amina, je veux publier une réponse à un sujet existant, afin de contribuer à la discussion, aider mes camarades et partager mes connaissances
  - Story F: En tant qu'Amina, je veux modifier ou supprimer mes propres sujets et réponses, afin de corriger des erreurs, mettre à jour des informations ou retirer du contenu obsolète
  - Story G: En tant qu'Amina, je veux consulter un tableau de bord montrant l'activité récente, afin de visualiser la vitalité de la communauté et identifier les discussions actives
  - Story H: En tant qu'Amina, je veux accéder à mon profil et me déconnecter en toute sécurité, afin de gérer mes informations personnelles et protéger mon compte

- **Persona Secondaire:** Dr. Miguel Rodriguez (Modérateur Académique)
  - Story I: En tant que Miguel, je veux créer, modifier et supprimer des catégories thématiques, afin d'organiser les discussions selon les besoins académiques évolutifs de la communauté
  - Story J: En tant que Miguel, je veux supprimer des sujets et réponses inappropriés tout en préservant l'historique (soft delete), afin de modérer efficacement sans perdre la traçabilité des décisions
  - Story K: En tant que Miguel, je veux visualiser des statistiques globales (nombre d'utilisateurs, sujets, réponses, catégories actives), afin de comprendre la santé de la communauté et identifier les tendances émergentes
  - Story L: En tant que Miguel, je veux consulter la liste des utilisateurs actifs et des dernières activités, afin de détecter rapidement les contenus nécessitant une attention particulière

## 4) Key Feature

### Fonctionnalités Obligatoires (MVP)

- **Authentification Locale Sécurisée:** Les utilisateurs créent un compte avec email unique, nom d'utilisateur, mot de passe haché et question/réponse de sécurité stockés localement dans SQLite ; la connexion valide les identifiants hachés et crée une session via SharedPreferences ; la récupération du mot de passe se fait localement en validant la réponse à la question de sécurité, permettant une réinitialisation autonome sans email
- **Gestion Complète des Discussions (CRUD):** Les utilisateurs connectés créent des sujets avec titre (obligatoire, min 5 caractères), contenu (obligatoire) et catégorie sélectionnée ; consultent la liste paginée des sujets avec titre, auteur, catégorie, date et aperçu ; accèdent au détail complet d'un sujet avec toutes ses réponses ; modifient ou suppriment (soft delete) leurs propres sujets si aucun autre utilisateur n'a répondu ou s'ils sont administrateurs
- **Système de Réponses:** Les utilisateurs connectés publient des réponses avec contenu (obligatoire, non vide) sur n'importe quel sujet ; consultent toutes les réponses affichées chronologiquement sous le sujet ; modifient ou suppriment (soft delete) leurs propres réponses ; les administrateurs peuvent supprimer n'importe quelle réponse inappropriée
- **Catégorisation Académique:** Les catégories (créées par les administrateurs ou pré-configurées) structurent tous les sujets ; chaque sujet appartient obligatoirement à une catégorie ; les utilisateurs filtrent la liste des sujets par catégorie pour accéder rapidement aux discussions pertinentes à leur domaine académique
- **Tableau de Bord d'Activité:** Les utilisateurs consultent un dashboard présentant les statistiques clés : nombre total de sujets actifs, nombre total de réponses, nombre de catégories, derniers sujets créés (5 plus récents) ; les administrateurs voient également le nombre d'utilisateurs actifs et les statistiques globales de modération
- **Gestion de Profil Utilisateur:** Les utilisateurs connectés accèdent à leur profil affichant nom, email, rôle (USER ou ADMIN), bio optionnelle et date de création ; ils modifient leur bio et consultent leurs contributions (sujets et réponses publiés) ; ils se déconnectent en supprimant la session locale

### Fonctionnalités Optionnelles (Améliorations)

- **Recherche Textuelle:** Une barre de recherche permet de trouver des sujets par mots-clés dans le titre ou le contenu via requêtes SQLite avec opérateur LIKE, facilitant la redécouverte rapide d'informations passées
- **Système de Likes:** Les utilisateurs connectés "aiment" des sujets pour exprimer leur intérêt ; le nombre de likes s'affiche sur chaque sujet ; les sujets populaires peuvent être mis en avant dans la liste ou le dashboard
- **Administration Avancée:** Un écran AdminActivity dédié permet aux administrateurs de gérer toutes les catégories, visualiser la liste complète des utilisateurs avec filtres, consulter des statistiques détaillées et accéder aux contenus signalés
- **Mode Sombre:** Une option dans les paramètres permet de basculer entre thème clair et thème sombre pour réduire la fatigue oculaire et s'adapter aux préférences utilisateur
- **Bio Enrichie:** Les utilisateurs personnalisent leur profil avec une biographie courte (max 200 caractères) affichée publiquement sur leurs contributions, renforçant l'identité communautaire

## 5) Key Flow

- **Exemple:** Premier lancement et inscription
  - **Trigger:** L'utilisateur lance l'application pour la première fois sur son appareil Android
  - **Path:** SplashActivity vérifie l'absence de session locale et redirige vers LoginActivity ; l'utilisateur clique sur "Créer un compte", remplit RegisterActivity avec nom, email, mot de passe, confirmation mot de passe, question et réponse de sécurité ; après validation (email unique, mots de passe identiques, champs non vides), le système hache le mot de passe et la réponse, insère l'utilisateur dans SQLite avec rôle USER, crée une session et redirige vers MainActivity
  - **Résultat:** L'utilisateur accède à l'écran d'accueil avec la liste des sujets et peut immédiatement commencer à participer

- **Exemple:** Création et publication d'un sujet
  - **Trigger:** Amina veut poser une question sur son cours de base de données
  - **Path:** Depuis MainActivity, elle clique sur le bouton flottant "Nouveau sujet", remplit CreateTopicActivity avec titre "Comment optimiser les requêtes SQL JOIN ?", contenu expliquant son problème, sélectionne la catégorie "Base de Données", et soumet ; le système valide les champs (titre >= 5 caractères, contenu non vide, catégorie sélectionnée), insère le sujet dans SQLite avec author_id = Amina, created_at = now, is_deleted = 0, et retourne à MainActivity où le sujet apparaît en tête de liste
  - **Résultat:** Son sujet est visible par tous les utilisateurs dans la catégorie "Base de Données" et elle peut consulter les réponses à venir

- **Exemple:** Consultation et réponse à un sujet
  - **Trigger:** Un étudiant veut répondre à la question d'Amina sur les requêtes SQL
  - **Path:** Depuis MainActivity, il clique sur le sujet "Comment optimiser les requêtes SQL JOIN ?" ; TopicDetailActivity affiche le titre, contenu, auteur, date, catégorie et la liste des réponses existantes (initialement vide) ; il clique sur "Ajouter une réponse", saisit son explication dans un champ texte et soumet ; le système valide que le contenu n'est pas vide, insère la réponse dans SQLite avec topic_id, author_id, created_at, et rafraîchit la liste des réponses
  - **Résultat:** Sa réponse apparaît immédiatement sous le sujet, visible par Amina et tous les autres utilisateurs consultant ce sujet

- **Exemple:** Récupération de mot de passe oublié
  - **Trigger:** Un utilisateur oublie son mot de passe et ne peut plus se connecter
  - **Path:** Depuis LoginActivity, il clique sur "Mot de passe oublié", saisit son email dans ForgotPasswordActivity ; le système vérifie que l'email existe dans SQLite, affiche la question de sécurité associée ; l'utilisateur saisit sa réponse ; le système hache la réponse, la compare au hash stocké, et si valide, permet de définir un nouveau mot de passe qui est haché et mis à jour dans SQLite
  - **Résultat:** L'utilisateur récupère l'accès à son compte de manière autonome et peut se reconnecter avec son nouveau mot de passe

- **Exemple:** Modération par un administrateur
  - **Trigger:** Miguel détecte un sujet contenant du contenu inapproprié
  - **Path:** Connecté avec un compte administrateur, Miguel consulte MainActivity, ouvre TopicDetailActivity du sujet problématique ; il clique sur le bouton "Supprimer" (visible uniquement pour les administrateurs) et confirme l'action ; le système met à jour is_deleted = 1 dans SQLite sans supprimer physiquement la ligne
  - **Résultat:** Le sujet disparaît de la liste visible par les utilisateurs réguliers mais reste traçable dans la base de données pour l'historique de modération

- **Exemple:** Consultation du tableau de bord
  - **Trigger:** Amina veut visualiser l'activité récente de la communauté
  - **Path:** Depuis le menu de navigation (bottom navigation ou drawer), elle sélectionne "Tableau de bord" ; DashboardActivity exécute des requêtes SQLite agrégées (COUNT) pour calculer le nombre de sujets actifs (is_deleted = 0), le nombre de réponses actives, le nombre de catégories, et récupère les 5 derniers sujets créés ; les cartes statistiques et la liste des dernières activités s'affichent
  - **Résultat:** Amina visualise clairement la vitalité de la communauté et peut cliquer sur un sujet récent pour y participer

- **Exemple:** Recherche de discussions passées
  - **Trigger:** Un étudiant cherche des discussions antérieures sur "Android SQLite"
  - **Path:** Depuis MainActivity, il saisit "SQLite" dans la barre de recherche et valide ; le système exécute une requête SQLite avec WHERE title LIKE '%SQLite%' OR content LIKE '%SQLite%' sur les sujets actifs, et affiche les résultats filtrés dans le RecyclerView
  - **Résultat:** Il accède directement aux discussions pertinentes sans parcourir manuellement toute la liste chronologique

## 6) Competitive Analysis

**Landscape (qui résout ce problème):**
- **Forums web classiques** (phpBB, Discourse) : solutions desktop/web nécessitant connexion permanente, utilisées par des communautés en ligne diverses
- **Réseaux sociaux universitaires** (Facebook Groups, WhatsApp Groups) : plateformes grand public mixant contenus personnels et académiques, très populaires chez les étudiants
- **Plateformes LMS** (Moodle, Canvas) : systèmes de gestion d'apprentissage cloud avec forums intégrés, déployés par les institutions académiques
- **Applications de prise de notes partagées** (Notion, Google Docs) : outils collaboratifs cloud pour documentation collective
- **Solution manuelle** : échanges par email, conversations en personne, notes physiques partagées

**Value Thesis (proposition de valeur de chaque acteur):**
- **Forums web:** "Organisez des discussions thématiques riches avec modération avancée" mais nécessitent serveur, connexion permanente et administration technique complexe
- **Réseaux sociaux:** "Connectez-vous instantanément avec vos camarades sur une plateforme familière" mais mélangent vie personnelle et académique, algorithmes imposent un ordre non chronologique, distractions publicitaires
- **Plateformes LMS:** "Centralisez cours, devoirs et discussions dans un environnement académique officiel" mais interfaces lourdes, dépendance stricte à Internet, accès contrôlé par l'institution
- **Outils collaboratifs cloud:** "Documentez et collaborez en temps réel sur des contenus structurés" mais nécessitent connexion permanente, courbe d'apprentissage élevée, inadaptés aux discussions rapides
- **Solution manuelle:** "Aucune barrière technologique, communication directe" mais informations dispersées, non traçables, inaccessibles en asynchrone

**Strengths / Weaknesses (forces/faiblesses de l'expérience):**
- **Forums web:** Forces = structure thématique excellente, historique consultable, modération granulaire ; Faiblesses = barrière d'accès (connexion obligatoire), complexité d'administration, interfaces souvent datées sur mobile
- **Réseaux sociaux:** Forces = adoption massive, onboarding instantané, notifications en temps réel ; Faiblesses = mélange contextes académiques/personnels, perte rapide d'information dans le flux, pas d'organisation thématique académique
- **Plateformes LMS:** Forces = intégration avec l'écosystème de cours, légitimité institutionnelle ; Faiblesses = UX lourde, inaccessible hors connexion, contrôle limité pour les étudiants
- **Outils cloud collaboratifs:** Forces = édition collaborative puissante, structuration flexible ; Faiblesses = dépendance Internet absolue, courbe d'apprentissage, inadaptés aux échanges rapides question/réponse

**Our Differentiators (nos points uniques):**
CampusForum combine les mots-clés de marque **Accessible** (fonctionnement 100% hors ligne grâce à SQLite local), **Académique** (interface sobre, catégories thématiques, modération dédiée), **Simple** (onboarding rapide, navigation intuitive adaptée aux débutants Android), **Sécurisé** (hachage local des mots de passe, récupération autonome par question de sécurité) et **Organisé** (catégorisation académique, tableau de bord d'activité). Notre trade-off assumé : pas de synchronisation multi-appareils ni de notifications push, en échange d'une autonomie totale sans dépendance serveur. Nous privilégions l'accessibilité immédiate dans les environnements à connexion instable plutôt que les fonctionnalités cloud avancées.

**Switching Costs & Risks (coûts de migration et risques):**
- **Résistance utilisateur:** Les étudiants habitués aux notifications push des réseaux sociaux pourraient initialement trouver l'application moins "vivante" ; mitigation : mettre en avant le tableau de bord d'activité pour encourager la consultation régulière
- **Perte de données historiques:** Les discussions existantes sur d'autres plateformes ne peuvent pas être migrées facilement ; mitigation : phase de transition où les utilisateurs sont encouragés à recréer les discussions importantes
- **Dépendance appareil unique:** Les utilisateurs perdent leurs données en changeant d'appareil ; mitigation : communiquer clairement cette limite dès l'inscription et cibler les utilisateurs valorisant la confidentialité locale
- **Risques d'usage incorrect:** Les utilisateurs pourraient oublier leurs réponses de sécurité et perdre définitivement l'accès ; mitigation : inciter à choisir des questions/réponses mémorables et prévenir clairement l'absence de récupération externe

**Notes (références):**
- Forums open-source : https://www.phpbb.com/, https://www.discourse.org/
- Analyse de l'usage mobile dans les universités : études montrant la prédominance du smartphone chez les étudiants (70%+ du temps connecté)
- Problématique de connectivité dans les campus africains et sud-américains : rapports UNESCO sur le digital divide dans l'éducation
- Exemples de forums Android locaux : peu de solutions existantes, niche non exploitée
