# Product Charter — CampusForum

## 1) Positionnement Produit

CampusForum est une application mobile Android native conçue pour les communautés universitaires, permettant aux étudiants, enseignants et membres académiques d'échanger et de collaborer via un forum local et accessible. Contrairement aux solutions cloud nécessitant une connexion permanente, CampusForum fonctionne entièrement hors ligne avec une base de données SQLite locale, garantissant ainsi un accès continu aux discussions académiques, même dans des environnements à connectivité limitée. L'application offre une expérience simple, sécurisée et adaptée aux besoins spécifiques d'une communauté étudiante.

## 2) Mots-clés de Marque

- **Accessible** — L'application fonctionne sans connexion Internet obligatoire, permettant à tous les étudiants de participer aux discussions, quelle que soit leur situation de connectivité
- **Académique** — L'interface sobre et professionnelle reflète le contexte universitaire et favorise des échanges constructifs et respectueux
- **Simple** — Une navigation claire et intuitive permet aux utilisateurs débutants de créer et consulter des discussions sans formation préalable
- **Sécurisé** — Les données personnelles et les mots de passe sont protégés localement avec des mécanismes de hachage et de récupération sécurisée
- **Organisé** — Un système de catégories et un tableau de bord structurent l'information et facilitent la découverte de contenus pertinents

## 3) Problème Central / JTBD

Lorsqu'un étudiant ou enseignant souhaite poser une question, partager une information ou participer à une discussion académique, il a besoin d'un espace communautaire accessible immédiatement, sans dépendre d'une connexion Internet ou d'infrastructures externes complexes. Les solutions existantes (forums web, réseaux sociaux, plateformes cloud) imposent souvent une connexion permanente, des processus d'inscription lourds, des distractions publicitaires ou des fonctionnalités inadaptées au contexte académique. Sans CampusForum, les étudiants perdent du temps à chercher des informations dispersées, sont frustrés par l'inaccessibilité des plateformes en cas de coupure réseau, et peinent à organiser efficacement les échanges par thématique académique.

## 4) Objectifs & Mission

- **Mission:** Créer un espace de dialogue académique mobile, local et autonome où chaque membre d'une communauté universitaire peut poser des questions, partager des connaissances et collaborer efficacement, indépendamment de sa connexion Internet.

- **Résultats Attendus (descriptifs):**
  - Les utilisateurs créent et consultent des discussions académiques instantanément, même sans connexion réseau
  - Les échanges restent organisés par catégories thématiques, facilitant la découverte et la recherche d'informations pertinentes
  - Les données personnelles et les contenus restent protégés localement, garantissant confidentialité et sécurité
  - L'interface claire et accessible permet aux étudiants débutants de participer sans barrière technique
  - Les administrateurs peuvent modérer les contenus et gérer les catégories pour maintenir un environnement académique de qualité
  - Les statistiques du tableau de bord offrent une visibilité sur l'activité de la communauté et encouragent l'engagement

## 5) Solutions Que Nous Offrons

### Authentification Locale Sécurisée

- **Ce que cela résout:** Permettre aux utilisateurs de créer un compte, se connecter et récupérer leur accès de manière autonome, sans infrastructure serveur ou connexion Internet
- **Parcours typique:** L'utilisateur s'inscrit avec email, mot de passe et question de sécurité ; les identifiants sont hachés et stockés localement dans SQLite ; en cas d'oubli, il répond à sa question de sécurité pour réinitialiser son mot de passe
- **Résultat pour l'utilisateur:** Un accès personnel sécurisé à la plateforme, avec possibilité de récupération autonome sans email ni intervention externe
- **Limites:** Pas de synchronisation multi-appareils ; les comptes restent locaux à l'appareil
- **Principes directeurs:** Sécurité locale / Autonomie / Simplicité
- **Références:** SharedPreferences pour la session, PasswordUtils pour le hachage

### Gestion des Discussions et Réponses

- **Ce que cela résout:** Permettre aux utilisateurs de créer des sujets de discussion, consulter les contributions existantes, publier des réponses et gérer leurs propres contenus
- **Parcours typique:** L'utilisateur crée un sujet avec titre, contenu et catégorie ; ce sujet apparaît dans la liste principale ; d'autres utilisateurs consultent le détail, ajoutent des réponses ; l'auteur peut modifier ou supprimer son contenu
- **Résultat pour l'utilisateur:** Une bibliothèque de discussions académiques structurées, consultables à tout moment, avec possibilité de contribuer et d'organiser l'information
- **Limites:** Pas de notifications push ; les discussions restent locales et ne sont pas partagées entre appareils
- **Principes directeurs:** Organisation / Participation / Contrôle éditorial
- **Références:** RecyclerView pour les listes, TopicDao/ReplyDao pour la persistance

### Catégorisation et Recherche

- **Ce que cela résout:** Structurer les discussions par thématiques académiques et faciliter la découverte de contenus pertinents via des filtres et une recherche par mots-clés
- **Parcours typique:** Les catégories sont créées par les administrateurs ou pré-configurées ; l'utilisateur choisit une catégorie lors de la création d'un sujet ; il peut filtrer la liste par catégorie ou rechercher par titre/contenu
- **Résultat pour l'utilisateur:** Une navigation intuitive dans les discussions, avec accès rapide aux sujets d'intérêt académique spécifique
- **Limites:** Recherche simple basée sur texte ; pas de recherche avancée ou tags multiples
- **Principes directeurs:** Clarté / Organisation / Découvrabilité
- **Références:** CategoryDao, mécanismes de filtrage SQLite

### Tableau de Bord d'Activité

- **Ce que cela résout:** Offrir une vue synthétique de l'activité communautaire et personnelle, encourageant l'engagement et la transparence
- **Parcours typique:** L'utilisateur accède au tableau de bord depuis le menu principal ; il visualise le nombre de sujets, réponses, catégories et dernières activités ; les administrateurs voient également les statistiques globales et les utilisateurs actifs
- **Résultat pour l'utilisateur:** Une compréhension claire de la vitalité de la communauté et de sa propre contribution, avec incitation à participer davantage
- **Limites:** Statistiques calculées localement en temps réel ; pas d'historiques graphiques complexes
- **Principes directeurs:** Transparence / Engagement / Simplicité
- **Références:** DashboardRepository avec requêtes SQLite agrégées

### Modération et Administration

- **Ce que cela résout:** Permettre aux administrateurs de maintenir un environnement académique sain en gérant les catégories, modérant les contenus inappropriés et supervisant l'activité
- **Parcours typique:** Un utilisateur avec rôle administrateur accède à l'écran d'administration ; il peut créer, modifier ou supprimer des catégories ; il peut supprimer des sujets ou réponses inappropriés ; il visualise la liste des utilisateurs et les statistiques globales
- **Résultat pour l'utilisateur:** Une communauté académique régulée, avec des thématiques pertinentes et des contenus de qualité
- **Limites:** Suppression logique (soft delete) pour préserver l'historique ; pas de bannissement d'utilisateurs ou de système de signalement avancé
- **Principes directeurs:** Qualité / Respect / Contrôle
- **Références:** AdminActivity, mécanismes de rôle USER/ADMIN dans SQLite
