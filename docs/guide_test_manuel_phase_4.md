# Guide de test manuel - Phase 4

Objectif : valider uniquement le parcours Home, categories, creation de sujet, liste, filtrage et detail de sujet dans Android Studio.

Donnees de test recommandees :
- Compte : `test.phase4@campus.local`
- Mot de passe : `phase4test`
- Nom utilisateur : `Test Phase4`
- Question de securite : `Couleur favorite ?`
- Reponse de securite : `bleu`
- Titre du sujet : `Question phase 4 sur SQLite`
- Contenu du sujet : `Je veux valider la creation et l affichage d un sujet depuis la phase 4.`
- Categorie : `Informatique`

## Etapes

1. Lancer l'application depuis Android Studio.
   - Action a effectuer : ouvrir le projet, selectionner l'emulateur deja disponible, puis cliquer sur Run pour lancer `app`.
   - Resultat attendu : l'application CampusForum s'ouvre sans crash et affiche l'ecran de connexion, ou directement Home si une session existe deja.
   - Probleme possible : l'application se ferme immediatement ou reste bloquee sur le lancement.
   - Correction probable ou piste de diagnostic : ouvrir Logcat et filtrer sur `AndroidRuntime` ou `CampusForum`; verifier aussi que la variante `app` est bien lancee et que l'installation APK s'est terminee.

2. Se connecter ou creer un compte si necessaire.
   - Action a effectuer : si l'ecran de connexion apparait, essayer de se connecter avec un compte existant. Si aucun compte n'existe, appuyer sur `Créer un compte`, renseigner les donnees de test, puis valider avec `Créer mon compte`.
   - Resultat attendu : la connexion ou la creation de compte reussit et l'utilisateur arrive dans l'application sans message d'erreur.
   - Probleme possible : message `E-mail ou mot de passe incorrect`, `Adresse e-mail invalide`, mot de passe trop court ou reponse de securite trop courte.
   - Correction probable ou piste de diagnostic : utiliser un e-mail valide, un mot de passe d'au moins 6 caracteres et une reponse de securite d'au moins 3 caracteres; si le compte existe deja, revenir a la connexion avec le meme mot de passe.

3. Verifier l'arrivee sur Home.
   - Action a effectuer : apres authentification, observer l'ecran principal.
   - Resultat attendu : l'ecran Home affiche `CampusForum`, le sous-titre des discussions academiques, le champ de recherche, les filtres de categories et le bouton `Créer un sujet`.
   - Probleme possible : l'application reste sur Login ou affiche un autre onglet.
   - Correction probable ou piste de diagnostic : verifier que `SessionManager` a cree une session; si un autre onglet est ouvert, appuyer sur `Accueil` dans la barre de navigation.

4. Verifier l'affichage des categories.
   - Action a effectuer : regarder la ligne de filtres en haut de Home et faire defiler horizontalement si besoin.
   - Resultat attendu : le filtre `Toutes` est visible et selectionne par defaut; les categories actives sont affichees, par exemple `Annonces`, `Base de Données`, `Général`, `Informatique`, `Mathématiques` et `Physique`.
   - Probleme possible : seules certaines categories apparaissent, ou aucune categorie n'apparait.
   - Correction probable ou piste de diagnostic : verifier `CategoryDao.getActiveCategories()` et l'insertion des categories par defaut dans `DatabaseContract.Categories.DEFAULT_CATEGORIES`; consulter Logcat avec le tag `CategoryDao`.

5. Verifier l'etat vide si aucun sujet n'existe.
   - Action a effectuer : sur une installation sans sujet, rester sur le filtre `Toutes` et laisser le champ de recherche vide.
   - Resultat attendu : la liste est masquee et l'etat vide affiche `Aucun sujet` avec `Soyez le premier à créer un sujet`.
   - Probleme possible : un ou plusieurs sujets sont deja affiches.
   - Correction probable ou piste de diagnostic : ce n'est pas forcement une anomalie si des tests precedents ont cree des sujets; effacer les donnees de l'application CampusForum depuis les parametres de l'emulateur, relancer l'application, puis recreer un compte de test.

6. Ouvrir l'ecran de creation de sujet.
   - Action a effectuer : appuyer sur le bouton `Créer un sujet` depuis Home.
   - Resultat attendu : `CreateTopicActivity` s'ouvre avec le titre `Nouveau sujet`, le champ `Titre du sujet`, la liste de categorie, le champ de contenu, le bouton `Publier` et le bouton `Annuler`.
   - Probleme possible : le bouton ne repond pas ou l'application crash.
   - Correction probable ou piste de diagnostic : verifier dans Logcat l'ouverture de `CreateTopicActivity`; confirmer que l'activite est declaree dans `AndroidManifest.xml` et que le clic `home_create_topic_button` est bien relie.

7. Creer un sujet complet.
   - Action a effectuer : saisir le titre de test, selectionner `Informatique`, saisir le contenu de test, puis appuyer sur `Publier`.
   - Resultat attendu : un message `Sujet créé avec succès` apparait et l'application revient a Home.
   - Probleme possible : message `Le titre doit contenir au moins 5 caractères`, `Le contenu doit contenir au moins 10 caractères`, `Sélectionnez une catégorie` ou `Session utilisateur introuvable`.
   - Correction probable ou piste de diagnostic : verifier que tous les champs sont remplis, que la categorie n'est pas restee sur `Sélectionnez une catégorie`, et que l'utilisateur est bien connecte.

8. Verifier le retour automatique sur Home.
   - Action a effectuer : apres publication, observer l'ecran affiche.
   - Resultat attendu : Home est de nouveau visible et le filtre actif reste coherent avec la navigation courante.
   - Probleme possible : l'ecran de creation reste ouvert apres publication.
   - Correction probable ou piste de diagnostic : verifier que `CreateTopicActivity` appelle bien `finish()` apres creation reussie; consulter Logcat pour une erreur SQLite au moment de l'insertion.

9. Verifier l'apparition du nouveau sujet dans la liste.
   - Action a effectuer : sur Home, garder `Toutes` selectionne et laisser la recherche vide.
   - Resultat attendu : la carte du sujet `Question phase 4 sur SQLite` apparait dans la liste.
   - Probleme possible : l'etat vide reste affiche ou la liste ne contient pas le nouveau sujet.
   - Correction probable ou piste de diagnostic : verifier que `HomeFragment.onResume()` recharge les sujets; controler `TopicDao.createTopic()` et `TopicDao.getActiveTopics()` dans Logcat ou au debug.

10. Verifier l'affichage correct de la carte sujet.
    - Action a effectuer : observer la carte du nouveau sujet dans la liste.
    - Resultat attendu : la carte affiche la categorie `Informatique`, le titre, un apercu du contenu, l'auteur au format `Par Test Phase4`, et une ligne de meta avec la date relative et `0 réponse`.
    - Probleme possible : categorie vide, auteur absent, mauvais compteur ou texte tronque de facon illisible.
    - Correction probable ou piste de diagnostic : verifier les donnees jointes dans `TopicDao.getTopics()` et le binding dans `TopicAdapter`; controler aussi les contraintes `maxLines` de `item_topic.xml`.

11. Filtrer par la categorie du sujet.
    - Action a effectuer : appuyer sur le filtre `Informatique`.
    - Resultat attendu : `Informatique` devient le filtre selectionne et le sujet cree reste visible.
    - Probleme possible : le sujet disparait apres selection de sa categorie.
    - Correction probable ou piste de diagnostic : verifier que l'id de categorie enregistre lors de la creation correspond a l'id du chip; controler `getActiveTopicsByCategory()` dans `TopicDao`.

12. Tester un filtre de categorie different.
    - Action a effectuer : appuyer sur une autre categorie, par exemple `Mathématiques` ou `Physique`.
    - Resultat attendu : le sujet `Question phase 4 sur SQLite` n'apparait plus si cette categorie ne contient aucun sujet correspondant; l'etat vide peut s'afficher.
    - Probleme possible : le sujet reste affiche dans une categorie differente.
    - Correction probable ou piste de diagnostic : verifier la clause de filtrage par categorie dans `TopicDao` et le rechargement de la liste apres clic sur un chip.

13. Revenir au filtre `Toutes`.
    - Action a effectuer : appuyer sur le filtre `Toutes`.
    - Resultat attendu : le filtre `Toutes` redevient selectionne et le sujet cree reapparait dans la liste globale.
    - Probleme possible : le filtre visuel change mais la liste ne se met pas a jour.
    - Correction probable ou piste de diagnostic : verifier que `selectedCategoryId` repasse a la valeur globale et que `loadTopics()` est appele apres le changement de filtre.

14. Ouvrir le detail du sujet.
    - Action a effectuer : appuyer sur la carte du sujet `Question phase 4 sur SQLite`.
    - Resultat attendu : `TopicDetailActivity` s'ouvre avec le bouton `Retour`, la categorie, le titre, l'auteur, la date de creation, le nombre de reponses et le contenu complet.
    - Probleme possible : clic sans effet, detail vide ou message `Sujet introuvable ou supprimé`.
    - Correction probable ou piste de diagnostic : verifier que `TopicAdapter` transmet le bon sujet au callback et que l'extra `EXTRA_TOPIC_ID` est bien envoye a `TopicDetailActivity`.

15. Verifier le nombre de reponses.
    - Action a effectuer : sur le detail, lire la ligne du compteur de reponses et regarder la section `Réponses`.
    - Resultat attendu : pour un sujet nouvellement cree, le compteur affiche `0 réponse` et l'etat `Aucune réponse pour le moment.` est visible.
    - Probleme possible : le compteur affiche une valeur differente ou l'etat vide des reponses n'apparait pas.
    - Correction probable ou piste de diagnostic : verifier `ReplyDao` et le calcul du nombre de reponses dans la requete des sujets; confirmer qu'aucune reponse n'a ete ajoutee pendant le test.

16. Revenir a Home depuis le detail.
    - Action a effectuer : appuyer sur `Retour` dans le detail du sujet.
    - Resultat attendu : l'application revient a Home, la liste reste accessible et le sujet cree est toujours visible avec le filtre `Toutes` ou le dernier filtre choisi.
    - Probleme possible : retour vers Login ou fermeture inattendue de l'application.
    - Correction probable ou piste de diagnostic : verifier que `TopicDetailActivity` termine seulement l'activite courante et que la session utilisateur n'est pas effacee.

17. Verifier l'absence de crash apres fermeture et reouverture.
    - Action a effectuer : fermer l'application depuis les applications recentes, puis relancer CampusForum depuis Android Studio ou depuis l'icone de l'application dans l'emulateur.
    - Resultat attendu : l'application se relance sans crash; si la session est conservee, Home apparait directement; sinon Login apparait et la connexion avec le compte de test permet de retrouver Home. Le sujet cree doit rester disponible dans la liste.
    - Probleme possible : crash au demarrage, session perdue anormalement ou sujet absent apres reouverture.
    - Correction probable ou piste de diagnostic : consulter Logcat au redemarrage; verifier `SplashActivity`, `SessionManager`, `DatabaseHelper` et la persistance SQLite de la table `topics`.

18. Valider le resultat global de la phase 4.
    - Action a effectuer : noter le resultat de chaque verification precedente.
    - Resultat attendu : tous les points de la phase 4 sont valides : lancement, authentification, Home, categories, etat vide, creation de sujet, liste, carte sujet, filtres, detail, compteur de reponses et relance sans crash.
    - Probleme possible : un seul point echoue alors que les autres passent.
    - Correction probable ou piste de diagnostic : isoler l'ecran concerne, reproduire avec une base vide puis avec une base contenant le sujet de test, et joindre les lignes Logcat pertinentes au rapport de test.
