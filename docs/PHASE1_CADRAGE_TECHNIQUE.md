# PHASE 1 - Cadrage Technique : Identité Visuelle Globale

**Date** : 10 juin 2026  
**Projet** : CampusForum - Application Android native Java/SQLite  
**Objectif** : Améliorer l'identité visuelle globale de manière minimaliste  
**Statut** : Cadrage - AUCUNE MODIFICATION

---

## 1️⃣ LOGO / MONOGRAMME CF

### État Actuel

- Launcher icon : Logo Android générique (mascotte Android)
- Fichiers : `mipmap-hdpi/ic_launcher.webp`, `mipmap-mdpi/ic_launcher.webp`, etc.
- Splash screen : Monogramme "CF" généré dynamiquement avec TextView + bg_chip_selected
- Login screen : Même monogramme "CF"

### Améliorations Requises

#### 🎯 Objectif

Remplacer le logo Android générique par un **monogramme "CF" stylisé** qui reflète l'identité Paraflow (accessible, académique, vert naturel).

#### ✅ À Créer

| Ressource                      | Type           | Emplacement                                | Spécifications                                       |
| ------------------------------ | -------------- | ------------------------------------------ | ---------------------------------------------------- |
| **ic_launcher_foreground.xml** | VectorDrawable | `drawable/`                                | Monogramme "CF" vectoriel, vert #5B8C5A, style épuré |
| **ic_launcher_background.xml** | Shape          | `drawable/` (optionnel)                    | Fond blanc pur #FFFFFF ou transparent                |
| **ic_launcher.webp**           | Image rastered | `mipmap-{hdpi,mdpi,xhdpi,xxhdpi,xxxhdpi}/` | Export CF vectoriel en 6 résolutions                 |
| **ic_launcher_round.webp**     | Image rastered | `mipmap-{hdpi,mdpi,xhdpi,xxhdpi,xxxhdpi}/` | Variante round du logo                               |

#### 📏 Spécifications du Monogramme "CF"

**Style recommandé** :

- **Forme** : Lettre/monogramme géométrique épuré
- **Couleur** : Vert #5B8C5A (cf_primary)
- **Typographie** : Sans-serif, weight 600+ (semibold/bold)
- **Taille des lettres** : C et F équilibrées, lisibles à petite taille (32dp minimum)
- **Logique** : Peut être "CF" superposé, entrecroisé, ou stylisé
- **Accessibilité** : Contraste ≥ 4.5:1 avec tous les fonds

**Dimensionnement** :

- Viewport : 108x108dp (standard Android)
- Zone sûre interne : 66x66dp (au moins)
- Export finaux :
  - mdpi (160dpi) : 48x48 px
  - hdpi (240dpi) : 72x72 px
  - xhdpi (320dpi) : 96x96 px
  - xxhdpi (480dpi) : 144x144 px
  - xxxhdpi (640dpi) : 192x192 px

#### 🚫 Ce qu'il ne faut PAS faire

- Ne pas utiliser la mascotte Android par défaut
- Ne pas faire trop complexe (rester simple et géométrique)
- Ne pas ajouter d'ombres ou de gradients (flat design)
- Ne pas dévier du vert #5B8C5A (rester cohérent avec Paraflow)

#### 📍 Impact

- **AndroidManifest.xml** : Reste `android:icon="@mipmap/ic_launcher"` inchangé
- **Splash screen** : Le monogramme "CF" reste un TextView (ne pas le remplacer par le launcher icon)
- **Navigation** : Aucun changement

---

## 2️⃣ SPLASH SCREEN

### État Actuel

Fichier : `layout/activity_splash.xml`

**Contenu** :

```
LinearLayout (vertical, centered)
├── TextView (monogramme "CF", 48dp, bg_chip_selected, vert primaire)
├── TextView (app_name, headline style)
├── TextView (tagline, body style)
├── ProgressBar (indéterminé, vert primaire)
└── TextView (offline note, caption style)
```

### Analyse

✅ **Ce qui est bon** :

- Centrée, équilibrée
- Utilise les styles existants
- Message offline approprié
- Couleur cohérente avec branding

⚠️ **Pistes d'amélioration** :

- Animation de la ProgressBar peut être améliorée visuellement
- Espacement entre éléments peut être affiné
- Le monogramme "CF" pourrait avoir une légère animation (optionnel)

### Défini pour Phase 1

#### ✅ À Conserver

- Structure globale (LinearLayout centré)
- Hiérarchie des textes
- Message offline
- Palette de couleurs
- Timing (~2-3 secondes avant navigation)

#### 🔧 À Améliorer (Minimaliste)

| Point           | Amélior.  | Détail                           | Impact |
| --------------- | --------- | -------------------------------- | ------ |
| **ProgressBar** | Optionnel | Tint color déjà #cf_primary, OK  | Visuel |
| **Spacing**     | Mineure   | Vérifier cf*spacing*\* cohérence | UX     |
| **Typography**  | Aucune    | Styles existants, OK             | -      |
| **Animation**   | Optionnel | Fade-in sur layout (Optional)    | UX     |

#### 📁 Fichiers Concernés

| Fichier                         | Action                   | Notes                       |
| ------------------------------- | ------------------------ | --------------------------- |
| `layout/activity_splash.xml`    | Revoir spacing/alignment | Pas de modification logique |
| `values/strings.xml`            | Aucune (textes OK)       | -                           |
| `values/dimens.xml`             | Aucune                   | Utilise spacing existant    |
| `drawable/bg_chip_selected.xml` | Aucune                   | OK pour monogramme CF       |

#### ⏱️ Timing

- **Splash duration** : 2-3 secondes
- **Condition navigation** : Après vérification session + chargement catégories
- **Ne pas ajouter** de splash trop long (frustration utilisateur)

---

## 3️⃣ PALETTE DE COULEURS

### État Actuel

Fichier : `values/colors.xml` - **31 couleurs déjà définies**

#### ✅ Couleurs à Conserver (100% OK)

**Primaires** :

- `cf_primary` (#5B8C5A) ← Usage : buttons primaires, accents
- `cf_primary_dark` (#4A7049) ← Usage : button pressed state
- `cf_primary_light` (#7BA77A) ← Usage : secondary actions, hover

**Surfaces** :

- `cf_background` (#FFFFFF) ← Usage : page background
- `cf_card` (#F8FAF8) ← Usage : card surfaces
- `cf_surface_secondary` (#F0F4F0) ← Usage : chip default, subtle separation
- `cf_input` (#E8EDE8) ← Usage : input background

**Textes** :

- `cf_text_primary` (#1A1A1A) ← Usage : headings, body principal
- `cf_text_secondary` (#4A4A4A) ← Usage : body secondary
- `cf_text_tertiary` (#737373) ← Usage : captions, metadata
- `cf_text_hint` (#A3A3A3) ← Usage : placeholder inputs

**Sémantiques** :

- `cf_on_primary` (#FFFFFF) ← Usage : text on primary surfaces
- `cf_on_background` (#1A1A1A) ← Usage : text on background
- `cf_divider` (#E8EDE8) ← Usage : separator lines
- `cf_disabled` (#DFE5DF) ← Usage : disabled state buttons

**Feedback** :

- `cf_error` (#F0D4D0) ← Usage : error container background
- `cf_success` (#D4E8D4) ← Usage : success container background
- `cf_warning` (#F5E8D4) ← Usage : warning container background
- `cf_error_text` (#7A2F2A) ← Usage : error text
- `cf_success_text` (#2F6B35) ← Usage : success text
- `cf_warning_text` (#7A5A22) ← Usage : warning text

### ⚠️ Corrections/Enrichissements Requis

#### 🔴 **CRITIQUE : Couleur Manquante**

**`bottom_nav_item_color`** → **À CRÉER**

Localisation : `values/color/` (créer nouveau dossier si nécessaire)  
OU dans `values/colors.xml` directement

**Options** :

- **Option 1** : ColorStateList (recommandé)
  - Inactif : #cf_text_tertiary (#737373)
  - Actif : #cf_primary (#5B8C5A)
  - Fichier : `color/bottom_nav_item_color.xml`

- **Option 2** : Couleur simple
  - Utiliser : #cf_text_tertiary (#737373)
  - Fichier : Dans `values/colors.xml` → `<color name="bottom_nav_item_color">#737373</color>`

**Recommandation** : Option 1 (StateList) = meilleure UX

#### 🟡 **À Créer : Couleurs pour Messages d'État**

| Couleur              | Hex                           | Usage               | Fichier    |
| -------------------- | ----------------------------- | ------------------- | ---------- |
| `message_error_bg`   | #F0D4D0 (existant cf_error)   | Fond message erreur | colors.xml |
| `message_success_bg` | #D4E8D4 (existant cf_success) | Fond message succès | colors.xml |
| `message_warning_bg` | #F5E8D4 (existant cf_warning) | Fond message alerte | colors.xml |

**Status** : Ces couleurs existent déjà, juste besoin de drawables associés.

#### 🟢 **À Enrichir : Palette Optionnelle (Pour Dark Mode)**

Fichier : `values-night/colors.xml` (à créer)

**Pour support dark mode futur** (Phase 2 optionnel) :

- `cf_background_dark` : #121212 (surface app dark)
- `cf_card_dark` : #1E1E1E (card dark)
- `cf_primary_on_dark` : #9EC89D (primary vert plus clair sur fond sombre)
- Adaptation textes : primary → #FFFFFF, secondary → #E0E0E0, etc.

**Note** : Ne pas implémenter en Phase 1, juste documenter.

### Vérification Contraste

✅ **Déjà conforme** (basé sur analyse Paraflow) :

- Vert #5B8C5A sur blanc #FFFFFF : ratio 4.89:1 ✅ (>4.5:1)
- Texte #1A1A1A sur blanc #FFFFFF : ratio 18.3:1 ✅
- Texte #737373 sur blanc #FFFFFF : ratio 4.77:1 ✅

---

## 4️⃣ STYLES GLOBAUX

### État Actuel

Fichier : `values/styles.xml` - **9 styles de widgets + 6 text appearances**

### Analyse Par Catégorie

#### 🔴 **CRITIQUE : À Ajouter**

| Style                                | Parent                                          | Usage                    | État                                       |
| ------------------------------------ | ----------------------------------------------- | ------------------------ | ------------------------------------------ |
| `Widget.CampusForum.FAB`             | MaterialComponents.ExtendedFloatingActionButton | Bouton flottant          | ❌ Manquant                                |
| `Widget.CampusForum.Button.Tertiary` | MaterialComponents.Button                       | Button tertiaire (ghost) | ❌ Manquant                                |
| `TextAppearance.CampusForum.Message` | MaterialComponents.Body2                        | Texte message générique  | ⚠️ Avoir 3 variantes Error/Success/Warning |

#### ✅ **À Conserver**

| Style                                        | Utilisé                             | État  |
| -------------------------------------------- | ----------------------------------- | ----- |
| `Widget.CampusForum.Button.Primary`          | Login, Register, Create Topic, etc. | ✅ OK |
| `Widget.CampusForum.Button.Secondary`        | Create Account, Cancel, etc.        | ✅ OK |
| `Widget.CampusForum.Input`                   | Tous les champs texte               | ✅ OK |
| `Widget.CampusForum.TextLink`                | Liens (Forgot Password, etc.)       | ✅ OK |
| `Widget.CampusForum.Card`                    | Item topics, replies, etc.          | ✅ OK |
| `Widget.CampusForum.Chip.Default`            | Chips non-sélectionnées             | ✅ OK |
| `Widget.CampusForum.Chip.Selected`           | Chips sélectionnées                 | ✅ OK |
| `TextAppearance.CampusForum.Headline`        | Page titles                         | ✅ OK |
| `TextAppearance.CampusForum.Title`           | Section titles                      | ✅ OK |
| `TextAppearance.CampusForum.Body`            | Body text                           | ✅ OK |
| `TextAppearance.CampusForum.BodySmall`       | Subtitles, metadata                 | ✅ OK |
| `TextAppearance.CampusForum.Caption`         | Timestamps, hints                   | ✅ OK |
| `TextAppearance.CampusForum.Message.Error`   | Error messages                      | ✅ OK |
| `TextAppearance.CampusForum.Message.Success` | Success messages                    | ✅ OK |

#### 🟡 **À Améliorer/Compléter**

| Style                              | Amélioration            | Détail                              |
| ---------------------------------- | ----------------------- | ----------------------------------- |
| `Widget.CampusForum.Input`         | Focus border visibility | Vérifier stroke width 1dp OK        |
| `Widget.CampusForum.Card`          | Ombre & elevation       | Vérifier MaterialCardView elevation |
| `Widget.CampusForum.Chip.Selected` | Animation pressé        | Mineure UX                          |

### ✏️ Nouveaux Styles à Créer

#### Style 1 : Widget.CampusForum.FAB

```xml
<style name="Widget.CampusForum.FAB" parent="Widget.MaterialComponents.FloatingActionButton.Primary">
    <item name="backgroundTint">@color/cf_primary</item>
    <item name="tint">@color/cf_on_primary</item>
    <item name="elevation">2dp</item>
</style>
```

**Usage** : `<FloatingActionButton style="@style/Widget.CampusForum.FAB" ... />`

#### Style 2 : Widget.CampusForum.Button.Tertiary (Optionnel)

```xml
<style name="Widget.CampusForum.Button.Tertiary" parent="Widget.MaterialComponents.Button.TextButton">
    <item name="android:textColor">@color/cf_primary</item>
    <item name="android:minHeight">@dimen/cf_button_height</item>
    <item name="android:textStyle">bold</item>
</style>
```

**Usage** : Boutons ghost/tertiary non prioritaires

### Style pour Messages d'État

**À créer dans `values/styles.xml`** :

```xml
<style name="Widget.CampusForum.Message.Container.Error" parent="@android:style/Widget">
    <item name="android:background">@drawable/bg_message_error</item>
    <item name="android:padding">@dimen/cf_spacing_4</item>
</style>

<style name="Widget.CampusForum.Message.Container.Success" parent="@android:style/Widget">
    <item name="android:background">@drawable/bg_message_success</item>
    <item name="android:padding">@dimen/cf_spacing_4</item>
</style>

<style name="Widget.CampusForum.Message.Container.Warning" parent="@android:style/Widget">
    <item name="android:background">@drawable/bg_message_warning</item>
    <item name="android:padding">@dimen/cf_spacing_4</item>
</style>
```

---

## 5️⃣ ICÔNES

### État Actuel

**Navigation Icons** (4 icônes) : `drawable/ic_nav_*.xml`

- `ic_nav_home.xml` ✅
- `ic_nav_dashboard.xml` ✅
- `ic_nav_profile.xml` ✅
- `ic_nav_admin.xml` ✅

**Autres** :

- `ic_dashboard_refresh.xml` ✅
- `ic_launcher_foreground.xml` (Android générique, à remplacer)

### Spécifications de Cohérence

#### 🎯 Normes à Respecter

| Critère                | Valeur                    | Justification                     |
| ---------------------- | ------------------------- | --------------------------------- |
| **Style**              | Outline/Line              | Flat, minimaliste (Paraflow)      |
| **Épaisseur de trait** | 2dp                       | Lisibilité à 24dp                 |
| **Viewport**           | 24x24dp                   | Standard Android Material         |
| **Couleur**            | Pas définie dans drawable | Utiliser `itemIconTint` ou `tint` |
| **Remplissage**        | Non (outline only)        | Cohérence visuelle                |

#### ✅ À Conserver

- Garder les 4 icônes navigation telles quelles
- Garder le style outline/vectoriel
- Garder la cohérence avec Paraflow

#### 🔧 À Améliorer

| Icône                      | Amélior.                     | Détail                                   |
| -------------------------- | ---------------------------- | ---------------------------------------- |
| `ic_nav_*.xml`             | Optionnel : Color state list | Couleur inactif vs actif (gray vs green) |
| `ic_dashboard_refresh.xml` | Vérifier size & style        | Assurer cohérence                        |

#### ❌ À Remplacer

| Icône                        | Raison                 | Nouveau                 |
| ---------------------------- | ---------------------- | ----------------------- |
| `ic_launcher_foreground.xml` | Logo Android générique | Monogramme CF vectoriel |

### 🟢 À Créer (Optionnel Phase 1)

| Icône                | Usage                              | Spéc                        |
| -------------------- | ---------------------------------- | --------------------------- |
| `ic_empty_state.xml` | Empty states (souhait, sujet vide) | Vectoriel, 48dp, vert léger |
| `ic_error.xml`       | Message erreur                     | Vectoriel, outline, rouge   |
| `ic_success.xml`     | Message succès                     | Vectoriel, outline, vert    |

**Note** : Ces icônes optionnelles pour améliorer UX des empty states.

---

## 6️⃣ ÉTATS VIDES (EMPTY STATES)

### Écrans Concernés

| Écran                | Fichier Layout              | État Vide Actuel            | Action CTA      |
| -------------------- | --------------------------- | --------------------------- | --------------- |
| **Home**             | `fragment_home.xml`         | ✅ Existe (titre + message) | Créer sujet     |
| **Dashboard**        | `fragment_dashboard.xml`    | À vérifier                  | N/A             |
| **Profile**          | `fragment_profile.xml`      | À vérifier                  | N/A             |
| **Admin Categories** | `fragment_admin.xml`        | À vérifier                  | N/A             |
| **Topic Replies**    | `activity_topic_detail.xml` | À vérifier                  | Ajouter réponse |

### État Vide Minimal : Home (Fragment)

**Fichier** : `layout/fragment_home.xml` (déjà présent)

**Structure actuelle** :

```
LinearLayout (id=home_empty_state)
├── TextView (title: "Aucun sujet")
└── TextView (message: "Soyez le premier...")
```

### Améliorations Recommandées

#### 🔧 Pour Chaque Empty State

| Élément        | Recomm.       | Implémentation                       |
| -------------- | ------------- | ------------------------------------ |
| **Icône**      | Ajouter       | Vectoriel 48dp, vert clair #7BA77A   |
| **Titre**      | Existent (OK) | Headline style, cf_text_primary      |
| **Message**    | Existent (OK) | Body small, cf_text_secondary        |
| **CTA Button** | Ajouter       | Button secondaire, invitation action |
| **Padding**    | Revoir        | Augmenter vertical (cf_spacing_6)    |

#### 📐 Template Empty State à Appliquer

```xml
<LinearLayout
    style="@style/Widget.CampusForum.Card"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="@dimen/cf_spacing_6">

    <!-- Optional Icon -->
    <ImageView
        android:id="@+id/empty_state_icon"
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:src="@drawable/ic_empty_state"
        android:contentDescription="@string/empty_state_icon"
        android:layout_marginBottom="@dimen/cf_spacing_4"
        android:tint="@color/cf_primary_light" />

    <!-- Title -->
    <TextView
        android:id="@+id/empty_state_title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textAppearance="@style/TextAppearance.CampusForum.Title"
        android:gravity="center"
        android:text="@string/empty_state_title" />

    <!-- Message -->
    <TextView
        android:id="@+id/empty_state_message"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textAppearance="@style/TextAppearance.CampusForum.BodySmall"
        android:gravity="center"
        android:layout_marginTop="@dimen/cf_spacing_2"
        android:text="@string/empty_state_message" />

    <!-- Optional CTA Button -->
    <Button
        android:id="@+id/empty_state_cta"
        style="@style/Widget.CampusForum.Button.Secondary"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="@dimen/cf_spacing_4"
        android:text="@string/empty_state_action" />
</LinearLayout>
```

#### 📝 Textes Empty State à Ajouter

Ajouter à `values/strings.xml` :

```xml
<!-- Empty States -->
<string name="empty_state_icon">Illustration état vide</string>
<string name="cf_home_empty_title">Aucun sujet pour le moment</string>
<string name="cf_home_empty_message">Soyez le premier à créer un sujet et lancer la discussion</string>
<string name="cf_home_empty_action">Créer un sujet</string>

<string name="cf_dashboard_empty_title">Aucune activité</string>
<string name="cf_dashboard_empty_message">Vos statistiques apparaîtront ici une fois actif</string>

<string name="cf_topic_replies_empty_title">Aucune réponse</string>
<string name="cf_topic_replies_empty_message">Soyez le premier à répondre à ce sujet</string>
<string name="cf_topic_replies_empty_action">Ajouter une réponse</string>
```

---

## 7️⃣ FICHIERS À MODIFIER

### Organisation Par Type

#### 📦 **Dossier `values/`**

| Fichier         | Action  | Détail                                                             |
| --------------- | ------- | ------------------------------------------------------------------ |
| **colors.xml**  | Ajouter | `bottom_nav_item_color` (couleur ou StateList) + doc               |
| **styles.xml**  | Ajouter | `Widget.CampusForum.FAB`, `Widget.CampusForum.Message.Container.*` |
| **strings.xml** | Ajouter | Textes empty states, libellés optionnels                           |
| **dimens.xml**  | Aucune  | Spacing/radii déjà OK                                              |
| **themes.xml**  | Aucune  | Thème Material3 OK                                                 |

#### 📦 **Dossier `color/`** (à créer si nécessaire)

| Fichier                       | Action | Détail                                    |
| ----------------------------- | ------ | ----------------------------------------- |
| **bottom_nav_item_color.xml** | Créer  | StateList pour bottom nav (inactif/actif) |

#### 📦 **Dossier `drawable/`**

| Fichier                        | Action             | Détail                         |
| ------------------------------ | ------------------ | ------------------------------ |
| **ic_launcher_foreground.xml** | Remplacer          | Monogramme CF vectoriel        |
| **bg_message_error.xml**       | Créer              | Shape avec bg #F0D4D0 + radius |
| **bg_message_success.xml**     | Créer              | Shape avec bg #D4E8D4 + radius |
| **bg_message_warning.xml**     | Créer              | Shape avec bg #F5E8D4 + radius |
| **ic_empty_state.xml**         | Créer (Optionnel)  | Icône illustrative 48dp        |
| **ic_nav_home.xml**            | Revoir (Optionnel) | Vérifier style & size          |
| **ic_nav_dashboard.xml**       | Revoir (Optionnel) | Vérifier style & size          |
| **ic_nav_profile.xml**         | Revoir (Optionnel) | Vérifier style & size          |
| **ic_nav_admin.xml**           | Revoir (Optionnel) | Vérifier style & size          |

#### 📦 **Dossier `mipmap-*/`** (toutes résolutions)

| Fichier                    | Action    | Détail                                      |
| -------------------------- | --------- | ------------------------------------------- |
| **ic_launcher.webp**       | Remplacer | Monogramme CF rastered (48-192px selon dpi) |
| **ic_launcher_round.webp** | Remplacer | Variante round (48-192px selon dpi)         |

**Résolutions à générer** :

- `mipmap-mdpi/` : 48x48 px
- `mipmap-hdpi/` : 72x72 px
- `mipmap-xhdpi/` : 96x96 px
- `mipmap-xxhdpi/` : 144x144 px
- `mipmap-xxxhdpi/` : 192x192 px

#### 📦 **Dossier `layout/`**

| Fichier                       | Action             | Détail                                   |
| ----------------------------- | ------------------ | ---------------------------------------- |
| **activity_splash.xml**       | Revoir             | Spacing/alignement (mineure)             |
| **fragment_home.xml**         | Améliorer          | Empty state avec icône + CTA             |
| **activity_main.xml**         | Corriger           | Référence `@color/bottom_nav_item_color` |
| **activity_login.xml**        | Revoir (Optionnel) | Cohérence visuelle                       |
| **activity_register.xml**     | Revoir (Optionnel) | Cohérence visuelle                       |
| **fragment_dashboard.xml**    | Améliorer          | Empty state si nécessaire                |
| **fragment_profile.xml**      | Améliorer          | Cohérence visuelle                       |
| **fragment_admin.xml**        | Améliorer          | Cohérence visuelle                       |
| **activity_topic_detail.xml** | Améliorer          | Empty state pour replies                 |

#### 📦 **Dossier `values-night/`** (Optionnel Phase 1)

| Fichier        | Action | Détail                                      |
| -------------- | ------ | ------------------------------------------- |
| **colors.xml** | Créer  | Palette dark mode (non prioritaire)         |
| **themes.xml** | Revoir | Retirer `forceDarkAllowed: false` optionnel |

#### 📦 **Manifest & Config**

| Fichier                 | Action | Détail                                |
| ----------------------- | ------ | ------------------------------------- |
| **AndroidManifest.xml** | Aucune | `icon="@mipmap/ic_launcher"` inchangé |
| **build.gradle.kts**    | Aucune | Aucune dépendance ajoutée             |

---

## 8️⃣ FICHIERS À NE PAS MODIFIER

### 🚫 Architecture & Logique Java

```
app/src/main/java/com/example/campusforum/
├── ui/auth/*                 [INTERDIT] Authentification
├── ui/topic/*                [INTERDIT] Logique topics (sauf layout cosmétique)
├── ui/dashboard/*            [INTERDIT] Logique dashboard
├── ui/profile/*              [INTERDIT] Logique profile
├── ui/admin/*                [INTERDIT] Logique admin
├── dao/*                      [INTERDIT] Data Access Layer
├── database/*                 [INTERDIT] SQLite schema
├── repository/*               [INTERDIT] Business logic
├── model/*                    [INTERDIT] Data models
├── adapter/*                  [INTERDIT] RecyclerView adapters
├── utils/SessionManager.java  [INTERDIT] Session management
├── utils/PasswordUtils.java   [INTERDIT] Password hashing
└── utils/DateUtils.java       [INTERDIT] Date utilities
```

### 🚫 Configuration Système

```
app/src/main/
├── AndroidManifest.xml        [INTERDIT] Application configuration
├── res/values/strings.xml     [PROTÉGÉ] Modifier UNIQUEMENT textes UI (pas de logique)
└── res/values/themes.xml      [PROTÉGÉ] Modifier UNIQUEMENT couleurs Material3 (pas de layout)

app/
├── build.gradle.kts           [INTERDIT] Dépendances & build
└── proguard-rules.pro         [INTERDIT] Build rules

build.gradle.kts               [INTERDIT] Project config
local.properties               [INTERDIT] Dev config
```

### 🚫 Données

```
campusforum.db                 [INTERDIT] SQLite database (local storage)
Schéma tables (users, topics, replies, categories)  [INTERDIT]
```

---

## 9️⃣ PLAN D'IMPLÉMENTATION MINIMAL

### Phases d'Implémentation (Ordre Strict)

#### **PHASE 1.1 : Ressources Globales (30 min)**

1. **Créer `color/bottom_nav_item_color.xml`** (StateList)
   - État inactif : #737373 (cf_text_tertiary)
   - État actif : #5B8C5A (cf_primary)

2. **Ajouter styles à `values/styles.xml`** :
   - `Widget.CampusForum.FAB`
   - `Widget.CampusForum.Button.Tertiary` (optionnel)
   - `Widget.CampusForum.Message.Container.Error/Success/Warning`

3. **Ajouter couleurs à `values/colors.xml`** :
   - Documenter utilisations existantes (éventuellement ajouter message\_\* color names si utile)

#### **PHASE 1.2 : Drawables de Message & Empty State (1 heure)**

4. **Créer `drawable/bg_message_error.xml`**
   - Shape : solid #F0D4D0, radius 8dp

5. **Créer `drawable/bg_message_success.xml`**
   - Shape : solid #D4E8D4, radius 8dp

6. **Créer `drawable/bg_message_warning.xml`**
   - Shape : solid #F5E8D4, radius 8dp

7. **Créer `drawable/ic_empty_state.xml`** (optionnel)
   - Vectoriel 48dp, illustratif (ex: lightbulb, folder vide, etc.)

#### **PHASE 1.3 : Monogramme CF & Launcher Icon (2-3 heures)**

8. **Créer `drawable/ic_launcher_foreground.xml`**
   - Monogramme CF vectoriel, viewport 108x108, vert #5B8C5A
   - Style épuré, géométrique

9. **Générer et exporter WebP pour mipmap**
   - Export depuis vectoriel en 5 résolutions (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)
   - `mipmap-*/ic_launcher.webp` (standard)
   - `mipmap-*/ic_launcher_round.webp` (round)

#### **PHASE 1.4 : Textes & Strings (30 min)**

10. **Ajouter strings à `values/strings.xml`** :
    - Empty state titles/messages/actions
    - Libellés optionnels

#### **PHASE 1.5 : Corrections Layout Critiques (30 min)**

11. **Corriger `layout/activity_main.xml`** :
    - Remplacer référence `@color/bottom_nav_item_color` (ou elle existe maintenant)
    - Vérifier compilation

12. **Améliorer `layout/fragment_home.xml`** :
    - Enrichir empty state avec icône + bouton CTA (si layout le permet)
    - Vérifier spacing cohérent

13. **Améliorer `layout/activity_splash.xml`** (optionnel) :
    - Revoir spacing si nécessaire
    - Tester visuel splash

#### **PHASE 1.6 : Amélioration Layout Optionnelle (2 heures)**

14. **Enrichir `layout/activity_login.xml`** :
    - Vérifier cohérence visuelle
    - Optionnel : améliorer empty state

15. **Enrichir `layout/activity_register.xml`** :
    - Vérifier cohérence visuelle

16. **Enrichir `layout/activity_topic_detail.xml`** :
    - Optionnel : empty state pour replies
    - Vérifier cohérence card replies

17. **Enrichir `layout/fragment_dashboard.xml`** :
    - Optionnel : empty state

### Timeline Estimée

| Phase                         | Durée     | Effort                  |
| ----------------------------- | --------- | ----------------------- |
| 1.1 - Ressources globales     | 30 min    | Facile                  |
| 1.2 - Drawables message/empty | 1 h       | Facile                  |
| 1.3 - Logo CF & mipmap        | 2-3 h     | Moyen (design + export) |
| 1.4 - Strings                 | 30 min    | Facile                  |
| 1.5 - Corrections critiques   | 30 min    | Facile                  |
| 1.6 - Amélioration layouts    | 2 h       | Facile-Moyen            |
| **TOTAL**                     | **6-7 h** |                         |

---

## 🔟 CRITÈRES DE VALIDATION

### Test de Validation Complet

#### ✅ **Lancement Application**

- [ ] Application compile sans erreur
- [ ] APK génère avec succès
- [ ] Aucun warning critique (build)
- [ ] Manifeste valide

#### ✅ **Splash Screen**

- [ ] Splash affiche monogramme CF (pas logo Android)
- [ ] Splash affiche "CampusForum" centré
- [ ] Splash affiche tagline "Forum académique local"
- [ ] Splash affiche offline note
- [ ] ProgressBar animée couleur verte
- [ ] Splash dure 2-3 sec puis navigue

#### ✅ **Login & Register**

- [ ] Logo CF monogramme visible
- [ ] Inputs stylisés cohérent (radius 8dp, bg #E8EDE8)
- [ ] Bouton primaire couleur verte #5B8C5A
- [ ] Bouton secondaire outline vert
- [ ] Lien "Mot de passe oublié?" style bleu/vert
- [ ] Messages erreur affiches (si applicable)
- [ ] Pas de regression logique authentification

#### ✅ **Home Screen**

- [ ] Titre "CampusForum" + sous-titre visibles
- [ ] Champ recherche style cohérent (input style OK)
- [ ] Chips catégories stylisées (default vs selected OK)
- [ ] Cartes topics affichées avec style `Widget.CampusForum.Card`
- [ ] Empty state enrichie : icône + titre + message + bouton
- [ ] FAB "+" visible, couleur verte, taille 56dp
- [ ] Spacing cohérent (16dp padding, 20dp spacing)

#### ✅ **Dashboard**

- [ ] Statistiques affichées (4 cartes : sujets, réponses, catégories, membres)
- [ ] Cartes stylisées cohérent
- [ ] Textes hierarchy OK (headline → body → caption)
- [ ] Pas de regression données

#### ✅ **Profile**

- [ ] Infos utilisateur affichées
- [ ] Photo utilisateur (si présente)
- [ ] Bio utilisateur affichée
- [ ] Styles cohérents avec reste app

#### ✅ **Admin** (si applicable)

- [ ] Page admin accessible (si user admin)
- [ ] Catégories affichées
- [ ] Styles cohérents
- [ ] Pas de regression logique admin

#### ✅ **États Vides**

- [ ] Home vide : icône + "Aucun sujet" + "Soyez le premier..." + bouton "Créer sujet"
- [ ] Dashboard vide (si applicable) : icône + message approprié
- [ ] Topic detail replies vide (si applicable) : message "Aucune réponse"
- [ ] Tous les empty states utilisent template cohérent

#### ✅ **Icônes & Logo**

- [ ] Launcher icon est monogramme CF (pas Android par défaut)
- [ ] Icônes navigation cohérentes (home, dashboard, profile, admin)
- [ ] Icones style outline, 24dp, épaisseur 2dp
- [ ] Icones bottom nav teintées correctement (inactif gray, actif vert)

#### ✅ **Palette Couleurs**

- [ ] Vert primaire #5B8C5A utilisé cohérent
- [ ] Blanc #FFFFFF fond pages
- [ ] Cartes #F8FAF8 separées visuellement
- [ ] Inputs #E8EDE8 distinguables
- [ ] Textes noir #1A1A1A primaire lisible
- [ ] Textes gris #4A4A4A secondaire lisible
- [ ] Contraste accessibility ≥ 4.5:1

#### ✅ **Absence Regression Fonctionnelle**

- [ ] Login/Register fonctionnent (authentication OK)
- [ ] Create/Edit/Delete topics fonctionnent (CRUD OK)
- [ ] Replies fonctionnent (CRUD OK)
- [ ] Dashboard données affichées (stats OK)
- [ ] Profile éditable (update OK)
- [ ] Admin features OK si user admin
- [ ] Navigation bottom OK (all tabs accessible)
- [ ] Search fonctionnel (si implémenté)
- [ ] Offline mode fonctionne (SQLite OK)

### Checklist Test Visuel

```markdown
## Visual Regression Test

### Consistency Check

- [ ] All buttons same height (48dp)
- [ ] All inputs same height (48dp)
- [ ] All chips same height (36dp)
- [ ] All card padding same (16dp)
- [ ] All text hierarchy consistent (headline/title/body/caption)
- [ ] All green color same (#5B8C5A)
- [ ] All radius consistent (6dp small, 8dp medium, 999dp full)

### Spacing Check

- [ ] Screen padding 16dp (cf_spacing_4)
- [ ] Section spacing 20dp (cf_section_spacing)
- [ ] Card spacing 16dp (cf_card_padding)
- [ ] No random spacing values

### Typography Check

- [ ] Headline 24sp bold
- [ ] Title 20sp bold
- [ ] Body 16sp regular
- [ ] BodySmall 14sp regular
- [ ] Caption 12sp regular
- [ ] No custom font sizes

### Color Check

- [ ] Primary buttons #5B8C5A
- [ ] Primary text #1A1A1A
- [ ] Secondary text #4A4A4A
- [ ] Tertiary text #737373
- [ ] Hint text #A3A3A3
- [ ] Card background #F8FAF8
- [ ] Input background #E8EDE8
- [ ] No random colors
```

### Procédure Test Complète

**Environnement** :

- Android Emulator (API 26 minimum, cible API 34)
- Orientation portrait + landscape (tester responsive)
- Densité écran : xhdpi (320 dpi) - standard test

**Pas de Test** :

1. Compiler & installer APK
2. Démarrer application → Vérifier splash 3sec
3. Attendre login screen → Vérifier cohérence UI
4. Login avec credentials valides → Home screen
5. Tester 4 tabs (Home, Dashboard, Profile, Admin)
6. Vérifier empty states
7. Créer sujet si possible (regr test CRUD)
8. Vérifier bottom navigation (4 items)
9. Vérifier launcher icon changé (long press home)
10. Vérifier offline mode si possible

**Signaler Regression Si** :

- ❌ Compilation fail
- ❌ Runtime crash
- ❌ Inconsistent colors
- ❌ Broken spacing
- ❌ Missing icons
- ❌ CRUD operations fail
- ❌ Authentication fail
- ❌ Wrong text hierarchy

---

## Résumé Exécutif

### Scope Phase 1

- ✅ Créer 6-8 fichiers drawables (message backgrounds, empty state icon, launcher icon)
- ✅ Ajouter 4 ressources color/styles
- ✅ Améliorer 2-3 layouts (home, splash, main)
- ✅ Ajouter 10-15 strings textes UI
- ⚠️ **Zéro modification logique Java**
- ⚠️ **Zéro modification SQLite/database**
- ⚠️ **Zéro modification authentification**

### Effort Estimé

**6-7 heures** (design + implémentation)

### Risques

- **Critique** : Compilation fail si bottom_nav_item_color non défini
- **Moyen** : Icône launcher si mal resized
- **Faible** : Spacing layouts si inconsistent

### Success Criteria

✅ Launcher icon = monogramme CF  
✅ Bottom nav coloration correcte  
✅ Empty states enrichis  
✅ Styles FAB créés  
✅ Messages d'état stylisés  
✅ Zéro regression fonctionnelle

---

**Document complété le 10 juin 2026**  
**Prêt pour phase d'implémentation**
