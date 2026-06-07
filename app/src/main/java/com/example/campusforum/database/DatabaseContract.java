package com.example.campusforum.database;

import android.provider.BaseColumns;

public final class DatabaseContract {

    public static final String DATABASE_NAME = "campusforum.db";
    public static final int DATABASE_VERSION = 1;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA = ", ";

    private DatabaseContract() {
    }

    public static final class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD_HASH = "password_hash";
        public static final String COLUMN_SECURITY_QUESTION = "security_question";
        public static final String COLUMN_SECURITY_ANSWER_HASH = "security_answer_hash";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_BIO = "bio";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_IS_ACTIVE = "is_active";

        public static final String ROLE_USER = "USER";
        public static final String ROLE_ADMIN = "ADMIN";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA +
                        COLUMN_USERNAME + TEXT_TYPE + NOT_NULL + COMMA +
                        COLUMN_EMAIL + TEXT_TYPE + NOT_NULL + " UNIQUE" + COMMA +
                        COLUMN_PASSWORD_HASH + TEXT_TYPE + NOT_NULL + COMMA +
                        COLUMN_SECURITY_QUESTION + TEXT_TYPE + NOT_NULL + COMMA +
                        COLUMN_SECURITY_ANSWER_HASH + TEXT_TYPE + NOT_NULL + COMMA +
                        COLUMN_ROLE + TEXT_TYPE + NOT_NULL + " DEFAULT '" + ROLE_USER + "' " +
                        "CHECK(" + COLUMN_ROLE + " IN ('" + ROLE_USER + "', '" + ROLE_ADMIN + "'))" + COMMA +
                        COLUMN_BIO + TEXT_TYPE + COMMA +
                        COLUMN_CREATED_AT + TEXT_TYPE + NOT_NULL + " DEFAULT CURRENT_TIMESTAMP" + COMMA +
                        COLUMN_IS_ACTIVE + INTEGER_TYPE + NOT_NULL + " DEFAULT 1" +
                        ")";

        static final String SQL_CREATE_EMAIL_INDEX =
                "CREATE INDEX idx_users_email ON " + TABLE_NAME + "(" + COLUMN_EMAIL + ")";

        static final String SQL_CREATE_ROLE_INDEX =
                "CREATE INDEX idx_users_role ON " + TABLE_NAME + "(" + COLUMN_ROLE + ")";

        private Users() {
        }
    }

    public static final class Categories implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_IS_ACTIVE = "is_active";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA +
                        COLUMN_NAME + TEXT_TYPE + NOT_NULL + " UNIQUE" + COMMA +
                        COLUMN_DESCRIPTION + TEXT_TYPE + COMMA +
                        COLUMN_CREATED_AT + TEXT_TYPE + NOT_NULL + " DEFAULT CURRENT_TIMESTAMP" + COMMA +
                        COLUMN_IS_ACTIVE + INTEGER_TYPE + NOT_NULL + " DEFAULT 1" +
                        ")";

        static final String SQL_CREATE_NAME_INDEX =
                "CREATE INDEX idx_categories_name ON " + TABLE_NAME + "(" + COLUMN_NAME + ")";

        public static final String[][] DEFAULT_CATEGORIES = {
                {"Informatique", "Questions et discussions sur la programmation et le développement"},
                {"Mathématiques", "Problèmes mathématiques, théorèmes et exercices"},
                {"Base de Données", "SQL, NoSQL et conception de bases de données"},
                {"Physique", "Mécanique, électricité et autres sujets de physique"},
                {"Annonces", "Annonces importantes et événements académiques"},
                {"Général", "Discussions générales et autres sujets"}
        };

        private Categories() {
        }
    }

    public static final class Topics implements BaseColumns {
        public static final String TABLE_NAME = "topics";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_AUTHOR_ID = "author_id";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
        public static final String COLUMN_IS_DELETED = "is_deleted";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA +
                        COLUMN_TITLE + TEXT_TYPE + NOT_NULL + COMMA +
                        COLUMN_CONTENT + TEXT_TYPE + NOT_NULL + COMMA +
                        COLUMN_AUTHOR_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                        COLUMN_CATEGORY_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                        COLUMN_CREATED_AT + TEXT_TYPE + NOT_NULL + " DEFAULT CURRENT_TIMESTAMP" + COMMA +
                        COLUMN_UPDATED_AT + TEXT_TYPE + COMMA +
                        COLUMN_IS_DELETED + INTEGER_TYPE + NOT_NULL + " DEFAULT 0" + COMMA +
                        "FOREIGN KEY(" + COLUMN_AUTHOR_ID + ") REFERENCES " +
                        Users.TABLE_NAME + "(" + Users._ID + ") ON DELETE CASCADE" + COMMA +
                        "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " +
                        Categories.TABLE_NAME + "(" + Categories._ID + ") ON DELETE RESTRICT" +
                        ")";

        static final String SQL_CREATE_AUTHOR_INDEX =
                "CREATE INDEX idx_topics_author ON " + TABLE_NAME + "(" + COLUMN_AUTHOR_ID + ")";

        static final String SQL_CREATE_CATEGORY_INDEX =
                "CREATE INDEX idx_topics_category ON " + TABLE_NAME + "(" + COLUMN_CATEGORY_ID + ")";

        static final String SQL_CREATE_DELETED_INDEX =
                "CREATE INDEX idx_topics_deleted ON " + TABLE_NAME + "(" + COLUMN_IS_DELETED + ")";

        static final String SQL_CREATE_CREATED_AT_INDEX =
                "CREATE INDEX idx_topics_created_at ON " + TABLE_NAME + "(" + COLUMN_CREATED_AT + " DESC)";

        private Topics() {
        }
    }

    public static final class Replies implements BaseColumns {
        public static final String TABLE_NAME = "replies";
        public static final String COLUMN_TOPIC_ID = "topic_id";
        public static final String COLUMN_AUTHOR_ID = "author_id";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATED_AT = "updated_at";
        public static final String COLUMN_IS_DELETED = "is_deleted";

        static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA +
                        COLUMN_TOPIC_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                        COLUMN_AUTHOR_ID + INTEGER_TYPE + NOT_NULL + COMMA +
                        COLUMN_CONTENT + TEXT_TYPE + NOT_NULL + COMMA +
                        COLUMN_CREATED_AT + TEXT_TYPE + NOT_NULL + " DEFAULT CURRENT_TIMESTAMP" + COMMA +
                        COLUMN_UPDATED_AT + TEXT_TYPE + COMMA +
                        COLUMN_IS_DELETED + INTEGER_TYPE + NOT_NULL + " DEFAULT 0" + COMMA +
                        "FOREIGN KEY(" + COLUMN_TOPIC_ID + ") REFERENCES " +
                        Topics.TABLE_NAME + "(" + Topics._ID + ") ON DELETE CASCADE" + COMMA +
                        "FOREIGN KEY(" + COLUMN_AUTHOR_ID + ") REFERENCES " +
                        Users.TABLE_NAME + "(" + Users._ID + ") ON DELETE CASCADE" +
                        ")";

        static final String SQL_CREATE_TOPIC_INDEX =
                "CREATE INDEX idx_replies_topic ON " + TABLE_NAME + "(" + COLUMN_TOPIC_ID + ")";

        static final String SQL_CREATE_AUTHOR_INDEX =
                "CREATE INDEX idx_replies_author ON " + TABLE_NAME + "(" + COLUMN_AUTHOR_ID + ")";

        static final String SQL_CREATE_DELETED_INDEX =
                "CREATE INDEX idx_replies_deleted ON " + TABLE_NAME + "(" + COLUMN_IS_DELETED + ")";

        static final String SQL_CREATE_CREATED_AT_INDEX =
                "CREATE INDEX idx_replies_created_at ON " + TABLE_NAME + "(" + COLUMN_CREATED_AT + " ASC)";

        private Replies() {
        }
    }
}
