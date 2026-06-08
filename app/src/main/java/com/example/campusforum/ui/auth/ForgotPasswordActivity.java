package com.example.campusforum.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusforum.R;
import com.example.campusforum.repository.AuthRepository;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText securityAnswerInput;
    private EditText newPasswordInput;
    private EditText passwordConfirmInput;
    private Button continueButton;
    private Button verifyButton;
    private Button resetButton;
    private View questionStep;
    private View passwordStep;
    private TextView securityQuestionText;
    private AuthRepository authRepository;
    private String verifiedEmail;
    private String verifiedSecurityAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        authRepository = new AuthRepository(this);
        bindViews();
        setupActions();
        showEmailStepOnly();
    }

    private void bindViews() {
        emailInput = findViewById(R.id.forgot_email_input);
        securityAnswerInput = findViewById(R.id.forgot_security_answer_input);
        newPasswordInput = findViewById(R.id.forgot_new_password_input);
        passwordConfirmInput = findViewById(R.id.forgot_password_confirm_input);
        continueButton = findViewById(R.id.forgot_continue_button);
        verifyButton = findViewById(R.id.forgot_verify_button);
        resetButton = findViewById(R.id.forgot_reset_button);

        questionStep = (View) securityAnswerInput.getParent();
        passwordStep = (View) newPasswordInput.getParent();
        securityQuestionText = (TextView) ((LinearLayout) questionStep).getChildAt(1);
    }

    private void setupActions() {
        continueButton.setOnClickListener(view -> loadSecurityQuestion());
        verifyButton.setOnClickListener(view -> verifySecurityAnswer());
        resetButton.setOnClickListener(view -> resetPassword());
    }

    private void showEmailStepOnly() {
        questionStep.setVisibility(View.GONE);
        passwordStep.setVisibility(View.GONE);
    }

    private void showQuestionStep() {
        questionStep.setVisibility(View.VISIBLE);
        passwordStep.setVisibility(View.GONE);
    }

    private void showPasswordStep() {
        questionStep.setVisibility(View.VISIBLE);
        passwordStep.setVisibility(View.VISIBLE);
    }

    private void loadSecurityQuestion() {
        emailInput.setError(null);
        String email = emailInput.getText().toString();
        if (email.trim().isEmpty()) {
            emailInput.setError(getString(R.string.cf_label_email));
            return;
        }

        continueButton.setEnabled(false);
        AuthRepository.AuthResult result = authRepository.getSecurityQuestion(email);
        continueButton.setEnabled(true);

        if (result.isSuccess()) {
            verifiedEmail = email;
            securityQuestionText.setText(result.getSecurityQuestion());
            showQuestionStep();
        } else {
            emailInput.setError(result.getMessage());
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void verifySecurityAnswer() {
        securityAnswerInput.setError(null);
        String answer = securityAnswerInput.getText().toString();
        if (answer.trim().isEmpty()) {
            securityAnswerInput.setError(getString(R.string.cf_label_security_answer));
            return;
        }

        verifyButton.setEnabled(false);
        AuthRepository.AuthResult result = authRepository.verifySecurityAnswer(verifiedEmail, answer);
        verifyButton.setEnabled(true);

        if (result.isSuccess()) {
            verifiedSecurityAnswer = answer;
            showPasswordStep();
        } else {
            securityAnswerInput.setError(result.getMessage());
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void resetPassword() {
        clearPasswordErrors();

        String newPassword = newPasswordInput.getText().toString();
        String confirmPassword = passwordConfirmInput.getText().toString();
        if (!validatePasswordFields(newPassword, confirmPassword)) {
            return;
        }

        resetButton.setEnabled(false);
        AuthRepository.AuthResult result = authRepository.resetPassword(
                verifiedEmail,
                verifiedSecurityAnswer,
                newPassword,
                confirmPassword);
        resetButton.setEnabled(true);

        if (result.isSuccess()) {
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            newPasswordInput.setError(result.getMessage());
            Toast.makeText(this, result.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatePasswordFields(String newPassword, String confirmPassword) {
        boolean valid = true;
        if (newPassword.isEmpty()) {
            newPasswordInput.setError(getString(R.string.cf_forgot_new_password));
            valid = false;
        }
        if (confirmPassword.isEmpty()) {
            passwordConfirmInput.setError(getString(R.string.cf_label_password_confirm));
            valid = false;
        }
        return valid;
    }

    private void clearPasswordErrors() {
        newPasswordInput.setError(null);
        passwordConfirmInput.setError(null);
    }
}
