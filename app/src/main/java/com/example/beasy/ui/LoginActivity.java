package com.example.beasy.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beasy.data.AppDatabase;
import com.example.beasy.data.entity.UserEntity;
import com.example.beasy.databinding.ActivityLoginBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private boolean isPasswordVisible = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
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

        // Login button
        binding.btnLogin.setOnClickListener(v -> attemptLogin());

        // "Don't have an account? Register" link
        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }

    private void attemptLogin() {
        String identifier = binding.etIdentifier.getText().toString().trim(); // username or email
        String password   = binding.etPassword.getText().toString().trim();

        // --- Validation ---
        if (identifier.isEmpty()) {
            binding.etIdentifier.setError("Username or email is required");
            binding.etIdentifier.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            binding.etPassword.requestFocus();
            return;
        }

        binding.btnLogin.setEnabled(false);
        binding.btnLogin.setText("Logging in...");

        // --- Room query on background thread ---
        executor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(this);

            // Try matching by email first, then by username
            // This lets users log in with either one
            UserEntity user = db.userDao().findByEmailAndPassword(identifier, password);
            if (user == null) {
                user = db.userDao().findByUsernameAndPassword(identifier, password);
            }

            final UserEntity matchedUser = user;

            runOnUiThread(() -> {
                binding.btnLogin.setEnabled(true);
                binding.btnLogin.setText("Login");

                if (matchedUser != null) {
                    // Save logged-in user's id to SharedPreferences
                    // MainActivity will read this next time the app opens
                    SharedPreferences prefs = getSharedPreferences("beasy_prefs", MODE_PRIVATE);
                    prefs.edit()
                            .putInt("logged_in_user_id", matchedUser.id)
                            .putString("logged_in_username", matchedUser.username)
                            .apply();

                    Toast.makeText(this, "Welcome back, " + matchedUser.username + "!", Toast.LENGTH_SHORT).show();

                    // TODO: Navigate to HomeActivity once built
                    // startActivity(new Intent(this, HomeActivity.class));
                    // finish();
                } else {
                    Toast.makeText(this, "❌ Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        binding = null;
    }
}