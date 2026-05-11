package com.example.beasy.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beasy.data.AppDatabase;
import com.example.beasy.data.entity.UserEntity;
import com.example.beasy.databinding.ActivityRegisterBinding;

import org.mindrot.jbcrypt.BCrypt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;

    private boolean isPasswordVisible = false;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding: inflate the layout and get a binding object
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Password toggle
        binding.ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                isPasswordVisible = false;
            } else {
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                isPasswordVisible = true;
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });

        // Register button
        binding.btnRegister.setOnClickListener(v -> attemptRegistration());

        // "Log in" link — navigates to LoginActivity
        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    private void attemptRegistration() {
        String username = binding.etUsername.getText().toString().trim();
        String email    = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();

        // Client-side validation (instant feedback, no DB hit needed)
        if (username.isEmpty()) {
            binding.etUsername.setError("Username is required");
            binding.etUsername.requestFocus();
            return;
        }
        if (username.length() < 5 || username.length() > 20) {
            binding.etUsername.setError("Username must be between 5 and 20 characters");
            binding.etUsername.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Enter a valid email address");
            binding.etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            binding.etPassword.requestFocus();
            return;
        }
        if (password.length() < 8 || password.length() > 30) {
            binding.etPassword.setError("Password must be between 8 and 30 characters");
            binding.etPassword.requestFocus();
            return;
        }

        // Disable button while working
        binding.btnRegister.setEnabled(false);
        binding.btnRegister.setText("Registering...");


        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getInstance(this);

                // Check if username is already taken
                if (db.userDao().findByUsername(username) != null) {
                    // runOnUiThread → go back to main thread to update UI
                    runOnUiThread(() -> {
                        binding.btnRegister.setEnabled(true);
                        binding.btnRegister.setText("Register");
                        binding.etUsername.setError("Username is already taken");
                        binding.etUsername.requestFocus();
                    });
                    return;
                }

                // Check if email is already registered
                if (db.userDao().findByEmail(email) != null) {
                    runOnUiThread(() -> {
                        binding.btnRegister.setEnabled(true);
                        binding.btnRegister.setText("Register");
                        binding.etEmail.setError("Email is already registered");
                        binding.etEmail.requestFocus();
                    });
                    return;
                }

                String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

                // All clear — insert the new user
                UserEntity newUser = new UserEntity(username, email, hashed);
                db.userDao().insertUser(newUser);

                // Success — update UI on main thread
                runOnUiThread(() -> {
                    Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    // Navigate to LoginActivity
                    startActivity(new Intent(this, LoginActivity.class));
                    finish(); // Remove RegisterActivity from back stack
                });

            } catch (Exception e) {
                // Unexpected DB error
                runOnUiThread(() -> {
                    binding.btnRegister.setEnabled(true);
                    binding.btnRegister.setText("Register");
                    Toast.makeText(this, "Something went wrong: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shut down the executor when the activity is destroyed to avoid memory leaks
        executor.shutdown();
        binding = null;
    }
}