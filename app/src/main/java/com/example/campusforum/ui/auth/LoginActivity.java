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

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authRepository = new AuthRepository(this);
        bindViews();
        setupActions();
    }

    private void bindViews() {
        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.login_password_input);
        loginButton = findViewById(R.id.login_button);
    }

    private void setupActions() {
        loginButton.setOnClickListener(view -> login());
        findViewById(R.id.login_create_account_button).setOnClickListener(view ->
                startActivity(new Intent(this, RegisterActivity.class)));
        findViewById(R.id.login_forgot_password_link).setOnClickListener(view ->
                startActivity(new Intent(this, ForgotPasswordActivity.class)));
    }

    private void login() {
        clearErrors();

        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        if (!validateInputs(email, password)) {
            return;
        }

        loginButton.setEnabled(false);
        AuthRepository.AuthResult result = authRepository.login(email, password);
        loginButton.setEnabled(true);

        if (result.isSuccess()) {
            openMainActivity();
        } else {
            passwordInput.setError(result.getMessage());
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String email, String password) {
        boolean valid = true;
        if (email.trim().isEmpty()) {
            emailInput.setError(getString(R.string.cf_label_email));
            valid = false;
        }
        if (password.isEmpty()) {
            passwordInput.setError(getString(R.string.cf_label_password));
            valid = false;
        }
        return valid;
    }

    private void clearErrors() {
        emailInput.setError(null);
        passwordInput.setError(null);
    }

    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
