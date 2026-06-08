package com.example.campusforum.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusforum.MainActivity;
import com.example.campusforum.R;
import com.example.campusforum.repository.AuthRepository;

public class RegisterActivity extends AppCompatActivity {

    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int MAX_USERNAME_LENGTH = 20;

    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private EditText securityQuestionInput;
    private EditText securityAnswerInput;
    private Button registerButton;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authRepository = new AuthRepository(this);
        bindViews();
        setupActions();
    }

    private void bindViews() {
        usernameInput = findViewById(R.id.register_username_input);
        emailInput = findViewById(R.id.register_email_input);
        passwordInput = findViewById(R.id.register_password_input);
        passwordConfirmInput = findViewById(R.id.register_password_confirm_input);
        securityQuestionInput = findViewById(R.id.register_security_question_input);
        securityAnswerInput = findViewById(R.id.register_security_answer_input);
        registerButton = findViewById(R.id.register_button);
    }

    private void setupActions() {
        registerButton.setOnClickListener(view -> register());
        findViewById(R.id.register_login_link).setOnClickListener(view -> finish());
    }

    private void register() {
        clearErrors();

        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String confirmPassword = passwordConfirmInput.getText().toString();
        String securityQuestion = securityQuestionInput.getText().toString().trim();
        String securityAnswer = securityAnswerInput.getText().toString().trim();

        if (!validateInputs(username, email, password, confirmPassword, securityQuestion, securityAnswer)) {
            return;
        }

        registerButton.setEnabled(false);
        AuthRepository.AuthResult result = authRepository.register(
                username,
                email,
                password,
                confirmPassword,
                securityQuestion,
                securityAnswer);
        registerButton.setEnabled(true);

        if (result.isSuccess()) {
            openMainActivity();
        } else {
            emailInput.setError(result.getMessage());
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String username, String email, String password,
            String confirmPassword, String securityQuestion, String securityAnswer) {
        boolean valid = true;

        if (username.isEmpty()) {
            usernameInput.setError(getString(R.string.cf_label_username));
            valid = false;
        } else if (username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
            usernameInput.setError("Le nom utilisateur doit contenir entre 3 et 20 caractères.");
            valid = false;
        }

        if (email.trim().isEmpty()) {
            emailInput.setError(getString(R.string.cf_label_email));
            valid = false;
        }
        if (password.isEmpty()) {
            passwordInput.setError(getString(R.string.cf_label_password));
            valid = false;
        }
        if (confirmPassword.isEmpty()) {
            passwordConfirmInput.setError(getString(R.string.cf_label_password_confirm));
            valid = false;
        }
        if (securityQuestion.isEmpty()) {
            securityQuestionInput.setError(getString(R.string.cf_label_security_question));
            valid = false;
        }
        if (securityAnswer.isEmpty()) {
            securityAnswerInput.setError(getString(R.string.cf_label_security_answer));
            valid = false;
        }

        return valid;
    }

    private void clearErrors() {
        usernameInput.setError(null);
        emailInput.setError(null);
        passwordInput.setError(null);
        passwordConfirmInput.setError(null);
        securityQuestionInput.setError(null);
        securityAnswerInput.setError(null);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
