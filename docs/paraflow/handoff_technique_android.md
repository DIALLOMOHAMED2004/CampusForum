# Handoff Technique - CampusForum Android (Java/SQLite)

## Vue d'Ensemble du Projet

**Nom :** CampusForum
**Type :** Application mobile Android native
**Langage :** Java
**Base de données :** SQLite locale
**Connectivité :** Offline-first, fonctionnement autonome sans Internet
**Plateforme cible :** Android 8.0 (API 26) minimum, cible Android 14 (API 34)
**IDE :** Android Studio Ladybug ou ultérieur

## Architecture Recommandée

### Structure des Packages

```
com.campusforum.app/
├── adapter/
│   ├── TopicAdapter.java
│   ├── ReplyAdapter.java
│   ├── CategoryAdapter.java
│   └── UserAdapter.java
├── dao/
│   ├── UserDao.java
│   ├── TopicDao.java
│   ├── ReplyDao.java
│   └── CategoryDao.java
├── database/
│   ├── DatabaseHelper.java
│   └── DatabaseContract.java
├── model/
│   ├── User.java
│   ├── Topic.java
│   ├── Reply.java
│   └── Category.java
├── repository/
│   ├── AuthRepository.java
│   ├── TopicRepository.java
│   ├── ReplyRepository.java
│   ├── DashboardRepository.java
│   └── CategoryRepository.java
├── ui/
│   ├── auth/
│   │   ├── SplashActivity.java
│   │   ├── LoginActivity.java
│   │   ├── RegisterActivity.java
│   │   └── ForgotPasswordActivity.java
│   ├── home/
│   │   └── MainActivity.java (avec HomeFragment)
│   ├── topic/
│   │   ├── TopicDetailActivity.java
│   │   ├── CreateTopicActivity.java
│   │   └── EditTopicActivity.java
│   ├── dashboard/
│   │   └── DashboardActivity.java
│   ├── profile/
│   │   └── ProfileActivity.java
│   └── admin/
│       └── AdminActivity.java (optionnel)
└── utils/
    ├── SessionManager.java
    ├── PasswordUtils.java
    └── DateUtils.java
```

## Modèle de Données SQLite

### Schéma de Base de Données

**Version de base de données :** 1
**Nom de base de données :** campusforum.db

#### Table : users

```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    security_question TEXT NOT NULL,
    security_answer_hash TEXT NOT NULL,
    role TEXT NOT NULL DEFAULT 'USER' CHECK(role IN ('USER', 'ADMIN')),
    bio TEXT,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    is_active INTEGER NOT NULL DEFAULT 1
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
```

**Champs :**
- `id` : Clé primaire auto-incrémentée
- `username` : Nom d'utilisateur visible (non unique, modifiable)
- `email` : Email unique, utilisé pour connexion
- `password_hash` : Mot de passe haché (utiliser SHA-256 ou BCrypt)
- `security_question` : Question de sécurité choisie lors de l'inscription
- `security_answer_hash` : Réponse de sécurité hachée
- `role` : Rôle utilisateur ('USER' ou 'ADMIN')
- `bio` : Biographie optionnelle (max 200 caractères)
- `created_at` : Date de création du compte (format ISO 8601)
- `is_active` : Compte actif (1) ou désactivé (0)

#### Table : categories

```sql
CREATE TABLE categories (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    description TEXT,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_categories_name ON categories(name);
```

**Champs :**
- `id` : Clé primaire auto-incrémentée
- `name` : Nom unique de la catégorie
- `description` : Description optionnelle de la catégorie
- `created_at` : Date de création (format ISO 8601)

**Catégories pré-configurées (données de démonstration) :**
```sql
INSERT INTO categories (name, description) VALUES
('Informatique', 'Questions et discussions sur la programmation, développement'),
('Mathématiques', 'Problèmes mathématiques, théorèmes, exercices'),
('Base de Données', 'SQL, NoSQL, conception de bases de données'),
('Physique', 'Mécanique, électricité, physique quantique'),
('Annonces', 'Annonces importantes, événements académiques'),
('Général', 'Discussions générales et autres sujets');
```

#### Table : topics

```sql
CREATE TABLE topics (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    author_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT,
    is_deleted INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

CREATE INDEX idx_topics_author ON topics(author_id);
CREATE INDEX idx_topics_category ON topics(category_id);
CREATE INDEX idx_topics_deleted ON topics(is_deleted);
CREATE INDEX idx_topics_created_at ON topics(created_at DESC);
```

**Champs :**
- `id` : Clé primaire auto-incrémentée
- `title` : Titre du sujet (min 5 caractères, max 100 caractères)
- `content` : Contenu du sujet (min 10 caractères, max 2000 caractères)
- `author_id` : ID de l'utilisateur auteur (FK vers users.id)
- `category_id` : ID de la catégorie (FK vers categories.id)
- `created_at` : Date de création (format ISO 8601)
- `updated_at` : Date de dernière modification (null si jamais modifié)
- `is_deleted` : Soft delete (0 = actif, 1 = supprimé)

**Contraintes :**
- ON DELETE CASCADE pour author_id : si l'utilisateur est supprimé, ses sujets le sont aussi
- ON DELETE RESTRICT pour category_id : empêche la suppression d'une catégorie contenant des sujets

#### Table : replies

```sql
CREATE TABLE replies (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    topic_id INTEGER NOT NULL,
    author_id INTEGER NOT NULL,
    content TEXT NOT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT,
    is_deleted INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_replies_topic ON replies(topic_id);
CREATE INDEX idx_replies_author ON replies(author_id);
CREATE INDEX idx_replies_deleted ON replies(is_deleted);
CREATE INDEX idx_replies_created_at ON replies(created_at ASC);
```

**Champs :**
- `id` : Clé primaire auto-incrémentée
- `topic_id` : ID du sujet parent (FK vers topics.id)
- `author_id` : ID de l'utilisateur auteur (FK vers users.id)
- `content` : Contenu de la réponse (min 1 caractère, max 1000 caractères)
- `created_at` : Date de création (format ISO 8601)
- `updated_at` : Date de dernière modification (null si jamais modifié)
- `is_deleted` : Soft delete (0 = actif, 1 = supprimé)

**Contraintes :**
- ON DELETE CASCADE : si le sujet ou l'utilisateur est supprimé, les réponses associées le sont aussi

#### Table : likes (Optionnel)

```sql
CREATE TABLE likes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    topic_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (topic_id) REFERENCES topics(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(topic_id, user_id)
);

CREATE INDEX idx_likes_topic ON likes(topic_id);
CREATE INDEX idx_likes_user ON likes(user_id);
```

**Champs :**
- `id` : Clé primaire auto-incrémentée
- `topic_id` : ID du sujet liké (FK vers topics.id)
- `user_id` : ID de l'utilisateur (FK vers users.id)
- `created_at` : Date du like (format ISO 8601)

**Contraintes :**
- UNIQUE(topic_id, user_id) : un utilisateur ne peut liker un sujet qu'une seule fois

## Composants Techniques Détaillés

### 1. DatabaseHelper.java

**Responsabilité :** Gestion de la base de données SQLite (création, mise à jour, version)

**Héritage :** Extends SQLiteOpenHelper

**Méthodes clés :**
```java
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "campusforum.db";
    private static final int DATABASE_VERSION = 1;

    // Singleton pattern
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Créer toutes les tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_TOPICS);
        db.execSQL(CREATE_TABLE_REPLIES);
        // Optionnel
        db.execSQL(CREATE_TABLE_LIKES);

        // Insérer catégories par défaut
        insertDefaultCategories(db);

        // Créer un utilisateur admin par défaut
        insertDefaultAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Migration logique si version change
    }
}
```

**Notes importantes :**
- Utiliser le pattern Singleton pour éviter les fuites mémoire
- Toujours fermer Cursor et Database après utilisation
- Gérer les transactions pour les opérations multiples

### 2. SessionManager.java

**Responsabilité :** Gestion de la session utilisateur (connexion, déconnexion, persistance)

**Stockage :** SharedPreferences

**Méthodes clés :**
```java
public class SessionManager {
    private static final String PREF_NAME = "CampusForumSession";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void createLoginSession(int userId, String username, String email, String role) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public String getRole() {
        return prefs.getString(KEY_ROLE, "USER");
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
```

### 3. PasswordUtils.java

**Responsabilité :** Hachage et vérification des mots de passe

**Algorithme recommandé :** SHA-256 (simple) ou BCrypt (meilleur mais plus complexe)

**Méthodes clés :**
```java
public class PasswordUtils {

    // Hachage SHA-256 simple (pour prototype rapide)
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // Vérification
    public static boolean verifyPassword(String password, String hashedPassword) {
        String hashOfInput = hashPassword(password);
        return hashOfInput.equals(hashedPassword);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
```

**Note sécurité :** Pour une application production, utiliser BCrypt avec salt. Pour ce projet universitaire, SHA-256 est acceptable.

### 4. UserDao.java

**Responsabilité :** Opérations CRUD sur la table users

**Méthodes principales :**
```java
public class UserDao {
    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // Créer un utilisateur
    public long createUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("email", user.getEmail());
        values.put("password_hash", user.getPasswordHash());
        values.put("security_question", user.getSecurityQuestion());
        values.put("security_answer_hash", user.getSecurityAnswerHash());
        values.put("role", user.getRole());
        values.put("created_at", user.getCreatedAt());

        long id = db.insert("users", null, values);
        db.close();
        return id;
    }

    // Vérifier si email existe
    public boolean emailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", new String[]{"id"},
            "email = ?", new String[]{email}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Récupérer utilisateur par email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null,
            "email = ? AND is_active = 1", new String[]{email},
            null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        cursor.close();
        db.close();
        return user;
    }

    // Récupérer utilisateur par ID
    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null,
            "id = ?", new String[]{String.valueOf(userId)},
            null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            user = cursorToUser(cursor);
        }
        cursor.close();
        db.close();
        return user;
    }

    // Mettre à jour la bio
    public int updateBio(int userId, String bio) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("bio", bio);
        int rows = db.update("users", values, "id = ?",
            new String[]{String.valueOf(userId)});
        db.close();
        return rows;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
        user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
        user.setPasswordHash(cursor.getString(cursor.getColumnIndexOrThrow("password_hash")));
        user.setSecurityQuestion(cursor.getString(cursor.getColumnIndexOrThrow("security_question")));
        user.setSecurityAnswerHash(cursor.getString(cursor.getColumnIndexOrThrow("security_answer_hash")));
        user.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
        user.setBio(cursor.getString(cursor.getColumnIndexOrThrow("bio")));
        user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));
        user.setActive(cursor.getInt(cursor.getColumnIndexOrThrow("is_active")) == 1);
        return user;
    }
}
```

### 5. TopicDao.java

**Responsabilité :** Opérations CRUD sur la table topics avec jointures

**Méthodes principales :**
```java
public class TopicDao {
    private DatabaseHelper dbHelper;

    public TopicDao(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    // Créer un sujet
    public long createTopic(Topic topic) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", topic.getTitle());
        values.put("content", topic.getContent());
        values.put("author_id", topic.getAuthorId());
        values.put("category_id", topic.getCategoryId());
        values.put("created_at", topic.getCreatedAt());

        long id = db.insert("topics", null, values);
        db.close();
        return id;
    }

    // Récupérer tous les sujets actifs avec détails auteur et catégorie
    public List<Topic> getAllActiveTopics() {
        List<Topic> topics = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT t.*, u.username as author_name, c.name as category_name, " +
                       "(SELECT COUNT(*) FROM replies WHERE topic_id = t.id AND is_deleted = 0) as reply_count " +
                       "FROM topics t " +
                       "INNER JOIN users u ON t.author_id = u.id " +
                       "INNER JOIN categories c ON t.category_id = c.id " +
                       "WHERE t.is_deleted = 0 " +
                       "ORDER BY t.created_at DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                topics.add(cursorToTopicWithDetails(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return topics;
    }

    // Rechercher des sujets par mots-clés
    public List<Topic> searchTopics(String keyword) {
        List<Topic> topics = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT t.*, u.username as author_name, c.name as category_name, " +
                       "(SELECT COUNT(*) FROM replies WHERE topic_id = t.id AND is_deleted = 0) as reply_count " +
                       "FROM topics t " +
                       "INNER JOIN users u ON t.author_id = u.id " +
                       "INNER JOIN categories c ON t.category_id = c.id " +
                       "WHERE t.is_deleted = 0 AND (t.title LIKE ? OR t.content LIKE ?) " +
                       "ORDER BY t.created_at DESC";

        String searchPattern = "%" + keyword + "%";
        Cursor cursor = db.rawQuery(query, new String[]{searchPattern, searchPattern});

        if (cursor.moveToFirst()) {
            do {
                topics.add(cursorToTopicWithDetails(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return topics;
    }

    // Filtrer par catégorie
    public List<Topic> getTopicsByCategory(int categoryId) {
        List<Topic> topics = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT t.*, u.username as author_name, c.name as category_name, " +
                       "(SELECT COUNT(*) FROM replies WHERE topic_id = t.id AND is_deleted = 0) as reply_count " +
                       "FROM topics t " +
                       "INNER JOIN users u ON t.author_id = u.id " +
                       "INNER JOIN categories c ON t.category_id = c.id " +
                       "WHERE t.is_deleted = 0 AND t.category_id = ? " +
                       "ORDER BY t.created_at DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});

        if (cursor.moveToFirst()) {
            do {
                topics.add(cursorToTopicWithDetails(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return topics;
    }

    // Récupérer un sujet par ID
    public Topic getTopicById(int topicId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT t.*, u.username as author_name, c.name as category_name, " +
                       "(SELECT COUNT(*) FROM replies WHERE topic_id = t.id AND is_deleted = 0) as reply_count " +
                       "FROM topics t " +
                       "INNER JOIN users u ON t.author_id = u.id " +
                       "INNER JOIN categories c ON t.category_id = c.id " +
                       "WHERE t.id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(topicId)});

        Topic topic = null;
        if (cursor.moveToFirst()) {
            topic = cursorToTopicWithDetails(cursor);
        }
        cursor.close();
        db.close();
        return topic;
    }

    // Mettre à jour un sujet
    public int updateTopic(int topicId, String title, String content, int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        values.put("category_id", categoryId);
        values.put("updated_at", DateUtils.getCurrentTimestamp());

        int rows = db.update("topics", values, "id = ?",
            new String[]{String.valueOf(topicId)});
        db.close();
        return rows;
    }

    // Suppression logique (soft delete)
    public int deleteTopic(int topicId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_deleted", 1);

        int rows = db.update("topics", values, "id = ?",
            new String[]{String.valueOf(topicId)});
        db.close();
        return rows;
    }

    private Topic cursorToTopicWithDetails(Cursor cursor) {
        Topic topic = new Topic();
        topic.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
        topic.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
        topic.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
        topic.setAuthorId(cursor.getInt(cursor.getColumnIndexOrThrow("author_id")));
        topic.setCategoryId(cursor.getInt(cursor.getColumnIndexOrThrow("category_id")));
        topic.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("created_at")));

        int updatedAtIndex = cursor.getColumnIndex("updated_at");
        if (updatedAtIndex != -1 && !cursor.isNull(updatedAtIndex)) {
            topic.setUpdatedAt(cursor.getString(updatedAtIndex));
        }

        topic.setAuthorName(cursor.getString(cursor.getColumnIndexOrThrow("author_name")));
        topic.setCategoryName(cursor.getString(cursor.getColumnIndexOrThrow("category_name")));
        topic.setReplyCount(cursor.getInt(cursor.getColumnIndexOrThrow("reply_count")));

        return topic;
    }
}
```

### 6. TopicAdapter.java (RecyclerView)

**Responsabilité :** Afficher la liste des sujets dans un RecyclerView

**Layout XML recommandé (item_topic.xml) :**
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:cardCornerRadius="8dp"
    app:cardElevation="2dp">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="12dp">

        <!-- Chip catégorie -->
        <com.google.android.material.chip.Chip
            android:id="@+id/chip_category"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginBottom="8dp"
            app:chipBackgroundColor="@color/category_color"/>

        <!-- Titre du sujet -->
        <TextView
            android:id="@+id/tv_title"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="16sp"
            android:textStyle="bold"
            android:maxLines="2"
            android:ellipsize="end"
            android:layout_marginBottom="4dp"/>

        <!-- Aperçu contenu -->
        <TextView
            android:id="@+id/tv_content_preview"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="14sp"
            android:maxLines="2"
            android:ellipsize="end"
            android:layout_marginBottom="8dp"/>

        <!-- Métadonnées -->
        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <TextView
                android:id="@+id/tv_author"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:textSize="12sp"
                android:drawableStart="@drawable/ic_person"
                android:drawablePadding="4dp"/>

            <TextView
                android:id="@+id/tv_date"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="12sp"
                android:layout_marginStart="8dp"/>

            <TextView
                android:id="@+id/tv_reply_count"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:textSize="12sp"
                android:drawableStart="@drawable/ic_comment"
                android:drawablePadding="4dp"
                android:layout_marginStart="8dp"/>
        </LinearLayout>
    </LinearLayout>
</androidx.cardview.widget.CardView>
```

**Adapter Java :**
```java
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private List<Topic> topics;
    private Context context;
    private OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onTopicClick(Topic topic);
    }

    public TopicAdapter(Context context, List<Topic> topics, OnTopicClickListener listener) {
        this.context = context;
        this.topics = topics;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        Topic topic = topics.get(position);
        holder.bind(topic);
    }

    @Override
    public int getItemCount() {
        return topics.size();
    }

    class TopicViewHolder extends RecyclerView.ViewHolder {
        Chip chipCategory;
        TextView tvTitle, tvContentPreview, tvAuthor, tvDate, tvReplyCount;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            chipCategory = itemView.findViewById(R.id.chip_category);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContentPreview = itemView.findViewById(R.id.tv_content_preview);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvReplyCount = itemView.findViewById(R.id.tv_reply_count);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onTopicClick(topics.get(position));
                }
            });
        }

        public void bind(Topic topic) {
            chipCategory.setText(topic.getCategoryName());
            tvTitle.setText(topic.getTitle());
            tvContentPreview.setText(topic.getContent());
            tvAuthor.setText(topic.getAuthorName());
            tvDate.setText(DateUtils.getRelativeTime(topic.getCreatedAt()));
            tvReplyCount.setText(String.valueOf(topic.getReplyCount()));
        }
    }
}
```

## Écrans et Navigation

### SplashActivity

**Objectif :** Vérifier la session et rediriger

**Logique :**
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    SessionManager sessionManager = new SessionManager(this);

    new Handler().postDelayed(() -> {
        Intent intent;
        if (sessionManager.isLoggedIn()) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }, 2000); // 2 secondes
}
```

### LoginActivity

**Validation :**
- Email non vide et format valide
- Mot de passe non vide (min 6 caractères)

**Logique :**
```java
private void loginUser() {
    String email = etEmail.getText().toString().trim();
    String password = etPassword.getText().toString().trim();

    // Validation
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        etEmail.setError("Email invalide");
        return;
    }

    if (password.length() < 6) {
        etPassword.setError("Mot de passe trop court");
        return;
    }

    // Vérification dans DB
    UserDao userDao = new UserDao(this);
    User user = userDao.getUserByEmail(email);

    if (user == null) {
        Toast.makeText(this, "Email inexistant", Toast.LENGTH_SHORT).show();
        return;
    }

    if (!PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
        Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_SHORT).show();
        return;
    }

    // Créer session
    SessionManager sessionManager = new SessionManager(this);
    sessionManager.createLoginSession(user.getId(), user.getUsername(),
                                     user.getEmail(), user.getRole());

    // Rediriger
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
}
```

### RegisterActivity

**Validation :**
- Nom d'utilisateur (3-20 caractères)
- Email unique et format valide
- Mot de passe (min 6 caractères, confirmation identique)
- Question et réponse de sécurité non vides

**Logique :**
```java
private void registerUser() {
    String username = etUsername.getText().toString().trim();
    String email = etEmail.getText().toString().trim();
    String password = etPassword.getText().toString();
    String confirmPassword = etConfirmPassword.getText().toString();
    String question = spinnerSecurityQuestion.getSelectedItem().toString();
    String answer = etSecurityAnswer.getText().toString().trim();

    // Validations...

    // Vérifier unicité email
    UserDao userDao = new UserDao(this);
    if (userDao.emailExists(email)) {
        Toast.makeText(this, "Email déjà utilisé", Toast.LENGTH_SHORT).show();
        return;
    }

    // Créer utilisateur
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPasswordHash(PasswordUtils.hashPassword(password));
    user.setSecurityQuestion(question);
    user.setSecurityAnswerHash(PasswordUtils.hashPassword(answer.toLowerCase()));
    user.setRole("USER");
    user.setCreatedAt(DateUtils.getCurrentTimestamp());

    long userId = userDao.createUser(user);

    if (userId > 0) {
        // Créer session automatique
        SessionManager sessionManager = new SessionManager(this);
        sessionManager.createLoginSession((int) userId, username, email, "USER");

        Toast.makeText(this, "Compte créé avec succès", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    } else {
        Toast.makeText(this, "Erreur lors de la création", Toast.LENGTH_SHORT).show();
    }
}
```

### MainActivity (avec BottomNavigationView)

**Layout XML (activity_main.xml) :**
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/fragment_container"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toTopOf="@id/bottom_navigation"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:menu="@menu/bottom_nav_menu"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

**Menu (bottom_nav_menu.xml) :**
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/nav_home"
        android:icon="@drawable/ic_home"
        android:title="Accueil"/>
    <item
        android:id="@+id/nav_dashboard"
        android:icon="@drawable/ic_dashboard"
        android:title="Tableau de Bord"/>
    <item
        android:id="@+id/nav_profile"
        android:icon="@drawable/ic_profile"
        android:title="Profil"/>
    <item
        android:id="@+id/nav_admin"
        android:icon="@drawable/ic_admin"
        android:title="Admin"
        android:visible="false"/>
</menu>
```

**Java :**
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

    // Afficher Admin si rôle ADMIN
    SessionManager sessionManager = new SessionManager(this);
    if (sessionManager.getRole().equals("ADMIN")) {
        bottomNav.getMenu().findItem(R.id.nav_admin).setVisible(true);
    }

    bottomNav.setOnItemSelectedListener(item -> {
        Fragment selectedFragment = null;

        if (item.getItemId() == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (item.getItemId() == R.id.nav_dashboard) {
            selectedFragment = new DashboardFragment();
        } else if (item.getItemId() == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        } else if (item.getItemId() == R.id.nav_admin) {
            selectedFragment = new AdminFragment();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();
        }

        return true;
    });

    // Charger HomeFragment par défaut
    if (savedInstanceState == null) {
        bottomNav.setSelectedItemId(R.id.nav_home);
    }
}
```

## Tests et Données de Démonstration

### Créer un utilisateur admin par défaut

```java
private void insertDefaultAdmin(SQLiteDatabase db) {
    ContentValues values = new ContentValues();
    values.put("username", "Admin");
    values.put("email", "admin@campus.edu");
    values.put("password_hash", PasswordUtils.hashPassword("admin123"));
    values.put("security_question", "Nom de votre école primaire ?");
    values.put("security_answer_hash", PasswordUtils.hashPassword("campusforum"));
    values.put("role", "ADMIN");
    values.put("created_at", DateUtils.getCurrentTimestamp());

    db.insert("users", null, values);
}
```

### Créer des sujets de démonstration

```java
private void insertDemoTopics(SQLiteDatabase db) {
    // Sujet 1
    ContentValues topic1 = new ContentValues();
    topic1.put("title", "Aide pour les requêtes SQL JOIN");
    topic1.put("content", "Bonjour, je cherche à comprendre comment optimiser les requêtes avec des JOIN multiples...");
    topic1.put("author_id", 1); // Admin
    topic1.put("category_id", 3); // Base de Données
    topic1.put("created_at", DateUtils.getCurrentTimestamp());
    db.insert("topics", null, topic1);

    // Sujet 2
    ContentValues topic2 = new ContentValues();
    topic2.put("title", "Annonce : Conférence IA le 15 mars");
    topic2.put("content", "Une conférence sur l'intelligence artificielle aura lieu le 15 mars...");
    topic2.put("author_id", 1);
    topic2.put("category_id", 5); // Annonces
    topic2.put("created_at", DateUtils.getCurrentTimestamp());
    db.insert("topics", null, topic2);
}
```

## Dépendances Gradle

**build.gradle (Module: app) :**
```gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // RecyclerView
    implementation 'androidx.recyclerview:recyclerview:1.3.2'

    // CardView
    implementation 'androidx.cardview:cardview:1.0.0'

    // Navigation (si utilisation NavController)
    implementation 'androidx.navigation:navigation-fragment:2.7.6'
    implementation 'androidx.navigation:navigation-ui:2.7.6'

    // Tests
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
}
```

## Bonnes Pratiques Android

1. **Toujours fermer les ressources :**
   ```java
   Cursor cursor = null;
   try {
       cursor = db.query(...);
       // Traiter cursor
   } finally {
       if (cursor != null) cursor.close();
   }
   ```

2. **Utiliser des Transactions pour opérations multiples :**
   ```java
   SQLiteDatabase db = dbHelper.getWritableDatabase();
   db.beginTransaction();
   try {
       // Opérations multiples
       db.setTransactionSuccessful();
   } finally {
       db.endTransaction();
   }
   ```

3. **Valider les entrées utilisateur côté client et DB :**
   - TextInputLayout avec setError()
   - Contraintes CHECK en SQLite

4. **Gérer les permissions (si nécessaire) :**
   - Pour écriture fichiers externes : WRITE_EXTERNAL_STORAGE
   - Pas nécessaire pour SQLite interne

5. **Utiliser AsyncTask ou Coroutines pour opérations DB :**
   ```java
   new AsyncTask<Void, Void, List<Topic>>() {
       @Override
       protected List<Topic> doInBackground(Void... voids) {
           return topicDao.getAllActiveTopics();
       }

       @Override
       protected void onPostExecute(List<Topic> topics) {
           adapter.updateData(topics);
       }
   }.execute();
   ```

6. **Gestion des erreurs :**
   - Try-catch pour SQLiteException
   - Messages utilisateur clairs
   - Logs pour debugging

## Checklist de Développement

### Phase 1 : Base de Données (Semaine 1)
- [ ] Créer DatabaseHelper avec toutes les tables
- [ ] Implémenter UserDao (CRUD complet)
- [ ] Implémenter CategoryDao
- [ ] Implémenter TopicDao
- [ ] Implémenter ReplyDao
- [ ] Créer données de démonstration
- [ ] Tester insertions et requêtes

### Phase 2 : Authentification (Semaine 2)
- [ ] Créer PasswordUtils (hachage)
- [ ] Créer SessionManager
- [ ] Implémenter SplashActivity
- [ ] Implémenter LoginActivity
- [ ] Implémenter RegisterActivity
- [ ] Implémenter ForgotPasswordActivity
- [ ] Tester flux complet d'authentification

### Phase 3 : Écrans Principaux (Semaine 3)
- [ ] Créer MainActivity avec BottomNavigationView
- [ ] Implémenter HomeFragment (liste sujets)
- [ ] Créer TopicAdapter
- [ ] Implémenter TopicDetailActivity
- [ ] Implémenter CreateTopicActivity
- [ ] Implémenter EditTopicActivity
- [ ] Ajouter recherche et filtres

### Phase 4 : Dashboard et Profil (Semaine 4)
- [ ] Implémenter DashboardFragment
- [ ] Créer requêtes statistiques
- [ ] Implémenter ProfileFragment
- [ ] Ajouter édition bio
- [ ] Tester déconnexion

### Phase 5 : Administration (Semaine 5 - Optionnel)
- [ ] Implémenter AdminFragment
- [ ] Ajouter gestion catégories
- [ ] Ajouter liste utilisateurs
- [ ] Ajouter modération contenus
- [ ] Tester toutes permissions

### Phase 6 : Finitions (Semaine 6)
- [ ] Appliquer un des 3 styles visuels choisis
- [ ] Optimiser performances (requêtes, animations)
- [ ] Ajouter gestion erreurs complète
- [ ] Tester sur plusieurs appareils
- [ ] Documenter le code
- [ ] Préparer démo

## Ressources Utiles

**Documentation Android :**
- [SQLiteOpenHelper](https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper)
- [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
- [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences)
- [Material Design Components](https://material.io/develop/android)

**Tutoriels recommandés :**
- "Android SQLite Database Tutorial" - GeeksforGeeks
- "Android RecyclerView Tutorial" - CodePath
- "Android Login and Registration" - SimplifiedCoding

## Contact et Support

Pour toute question technique ou clarification sur ce handoff, consulter :
- Documentation Android officielle
- Stack Overflow (tag: android, sqlite, java)
- Forums étudiants de votre université

**Bonne chance avec le développement de CampusForum ! 🚀**
