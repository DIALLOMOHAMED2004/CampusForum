package com.example.campusforum.repository;

import android.content.Context;
import android.util.Patterns;

import com.example.campusforum.dao.UserDao;
import com.example.campusforum.database.DatabaseContract;
import com.example.campusforum.model.User;
import com.example.campusforum.utils.PasswordUtils;
import com.example.campusforum.utils.SessionManager;

import java.util.Locale;

public class AuthRepository {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_SECURITY_ANSWER_LENGTH = 3;

    private final UserDao userDao;
    private final SessionManager sessionManager;

    public AuthRepository(Context context) {
        userDao = new UserDao(context);
        sessionManager = new SessionManager(context);
    }

    public AuthResult register(String username, String email, String password,
            String confirmPassword, String securityQuestion, String securityAnswer) {
        String normalizedUsername = normalizeText(username);
        String normalizedEmail = normalizeEmail(email);
        String normalizedQuestion = normalizeText(securityQuestion);
        String normalizedAnswer = normalizeText(securityAnswer);

        AuthResult validationResult = validateRegistration(
                normalizedUsername,
                normalizedEmail,
                password,
                confirmPassword,
                normalizedQuestion,
                normalizedAnswer);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        if (userDao.emailExists(normalizedEmail)) {
            return AuthResult.failure("Cette adresse e-mail est déjà utilisée.");
        }

        User user = new User();
        user.setUsername(normalizedUsername);
        user.setEmail(normalizedEmail);
        user.setPasswordHash(PasswordUtils.hashPassword(password));
        user.setSecurityQuestion(normalizedQuestion);
        user.setSecurityAnswerHash(PasswordUtils.hashSecurityAnswer(normalizedAnswer));
        user.setRole(DatabaseContract.Users.ROLE_USER);
        user.setActive(true);

        long userId = userDao.createUser(user);
        if (userId <= 0) {
            return AuthResult.failure("Impossible de créer le compte.");
        }

        sessionManager.createSession(userId, normalizedUsername, normalizedEmail, DatabaseContract.Users.ROLE_USER);
        return AuthResult.success(
                "Compte créé avec succès.",
                new AuthUser(userId, normalizedUsername, normalizedEmail, DatabaseContract.Users.ROLE_USER),
                null);
    }

    public AuthResult login(String email, String password) {
        String normalizedEmail = normalizeEmail(email);
        AuthResult validationResult = validateLogin(normalizedEmail, password);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        User user = userDao.getUserByEmail(normalizedEmail);
        if (user == null || !user.isActive()) {
            return AuthResult.failure("E-mail ou mot de passe incorrect.");
        }

        if (!PasswordUtils.verifyPassword(password, user.getPasswordHash())) {
            return AuthResult.failure("E-mail ou mot de passe incorrect.");
        }

        sessionManager.createSession(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        return AuthResult.success(
                "Connexion réussie.",
                new AuthUser(user.getId(), user.getUsername(), user.getEmail(), user.getRole()),
                null);
    }

    public void logout() {
        sessionManager.clearSession();
    }

    public boolean emailExists(String email) {
        String normalizedEmail = normalizeEmail(email);
        return isValidEmail(normalizedEmail) && userDao.emailExists(normalizedEmail);
    }

    public AuthResult getSecurityQuestion(String email) {
        String normalizedEmail = normalizeEmail(email);
        if (!isValidEmail(normalizedEmail)) {
            return AuthResult.failure("Adresse e-mail invalide.");
        }

        User user = userDao.getUserByEmail(normalizedEmail);
        if (user == null || !user.isActive()) {
            return AuthResult.failure("Aucun compte actif trouvé pour cette adresse e-mail.");
        }

        String securityQuestion = userDao.getSecurityQuestionByEmail(normalizedEmail);
        if (isBlank(securityQuestion)) {
            return AuthResult.failure("Question de sécurité introuvable.");
        }

        return AuthResult.success("Question de sécurité trouvée.", null, securityQuestion);
    }

    public AuthResult verifySecurityAnswer(String email, String securityAnswer) {
        String normalizedEmail = normalizeEmail(email);
        String normalizedAnswer = normalizeText(securityAnswer);

        AuthResult validationResult = validateSecurityAnswer(normalizedEmail, normalizedAnswer);
        if (!validationResult.isSuccess()) {
            return validationResult;
        }

        User user = userDao.getUserByEmail(normalizedEmail);
        if (user == null || !user.isActive()) {
            return AuthResult.failure("Réponse de sécurité incorrecte.");
        }

        if (!PasswordUtils.verifySecurityAnswer(normalizedAnswer, user.getSecurityAnswerHash())) {
            return AuthResult.failure("Réponse de sécurité incorrecte.");
        }

        return AuthResult.success("Réponse de sécurité vérifiée.", null, user.getSecurityQuestion());
    }

    public AuthResult resetPassword(String email, String securityAnswer,
            String newPassword, String confirmPassword) {
        String normalizedEmail = normalizeEmail(email);
        String normalizedAnswer = normalizeText(securityAnswer);

        AuthResult answerResult = verifySecurityAnswer(normalizedEmail, normalizedAnswer);
        if (!answerResult.isSuccess()) {
            return answerResult;
        }

        AuthResult passwordResult = validatePasswordChange(newPassword, confirmPassword);
        if (!passwordResult.isSuccess()) {
            return passwordResult;
        }

        User user = userDao.getUserByEmail(normalizedEmail);
        if (user == null || !user.isActive()) {
            return AuthResult.failure("Impossible de réinitialiser le mot de passe.");
        }

        boolean updated = userDao.updatePassword(user.getId(), PasswordUtils.hashPassword(newPassword));
        if (!updated) {
            return AuthResult.failure("Impossible de réinitialiser le mot de passe.");
        }

        return AuthResult.success("Mot de passe réinitialisé avec succès.", null, null);
    }

    private AuthResult validateRegistration(String username, String email, String password,
            String confirmPassword, String securityQuestion, String securityAnswer) {
        if (isBlank(username) || isBlank(email) || isBlank(password) || isBlank(confirmPassword)
                || isBlank(securityQuestion) || isBlank(securityAnswer)) {
            return AuthResult.failure("Tous les champs sont obligatoires.");
        }
        if (!isValidEmail(email)) {
            return AuthResult.failure("Adresse e-mail invalide.");
        }
        AuthResult passwordResult = validatePasswordChange(password, confirmPassword);
        if (!passwordResult.isSuccess()) {
            return passwordResult;
        }
        if (securityAnswer.length() < MIN_SECURITY_ANSWER_LENGTH) {
            return AuthResult.failure("La réponse de sécurité est trop courte.");
        }
        return AuthResult.success("Validation réussie.", null, null);
    }

    private AuthResult validateLogin(String email, String password) {
        if (isBlank(email) || isBlank(password)) {
            return AuthResult.failure("E-mail et mot de passe obligatoires.");
        }
        if (!isValidEmail(email)) {
            return AuthResult.failure("Adresse e-mail invalide.");
        }
        return AuthResult.success("Validation réussie.", null, null);
    }

    private AuthResult validateSecurityAnswer(String email, String securityAnswer) {
        if (!isValidEmail(email)) {
            return AuthResult.failure("Adresse e-mail invalide.");
        }
        if (isBlank(securityAnswer)) {
            return AuthResult.failure("Réponse de sécurité obligatoire.");
        }
        if (securityAnswer.length() < MIN_SECURITY_ANSWER_LENGTH) {
            return AuthResult.failure("La réponse de sécurité est trop courte.");
        }
        return AuthResult.success("Validation réussie.", null, null);
    }

    private AuthResult validatePasswordChange(String password, String confirmPassword) {
        if (password == null || confirmPassword == null) {
            return AuthResult.failure("Mot de passe obligatoire.");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            return AuthResult.failure("Le mot de passe doit contenir au moins 6 caractères.");
        }
        if (!password.equals(confirmPassword)) {
            return AuthResult.failure("Les mots de passe ne correspondent pas.");
        }
        return AuthResult.success("Validation réussie.", null, null);
    }

    private String normalizeEmail(String email) {
        if (email == null) {
            return "";
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String normalizeText(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return !isBlank(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static final class AuthResult {
        private final boolean success;
        private final String message;
        private final AuthUser user;
        private final String securityQuestion;

        private AuthResult(boolean success, String message, AuthUser user, String securityQuestion) {
            this.success = success;
            this.message = message;
            this.user = user;
            this.securityQuestion = securityQuestion;
        }

        public static AuthResult success(String message, AuthUser user, String securityQuestion) {
            return new AuthResult(true, message, user, securityQuestion);
        }

        public static AuthResult failure(String message) {
            return new AuthResult(false, message, null, null);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public AuthUser getUser() {
            return user;
        }

        public String getSecurityQuestion() {
            return securityQuestion;
        }
    }

    public static final class AuthUser {
        private final long id;
        private final String username;
        private final String email;
        private final String role;

        private AuthUser(long id, String username, String email, String role) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.role = role;
        }

        public long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }
    }
}
