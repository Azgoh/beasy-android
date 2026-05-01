package com.example.beasy.ui;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beasy.R;
import com.example.beasy.api.ApiClient;
import com.example.beasy.api.ApiService;
import com.example.beasy.model.RegisterRequest;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword;
    private Button btnRegister;
    private ImageView ivTogglePassword;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        etUsername       = findViewById(R.id.etUsername);
        etEmail          = findViewById(R.id.etEmail);
        etPassword       = findViewById(R.id.etPassword);
        btnRegister      = findViewById(R.id.btnRegister);
        ivTogglePassword = findViewById(R.id.ivTogglePassword);

        ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ivTogglePassword.setImageResource(android.R.drawable.ic_menu_view); // eye-slash icon
                isPasswordVisible = false;
            } else {
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivTogglePassword.setImageResource(android.R.drawable.ic_menu_view);
                isPasswordVisible = true;
            }
            // Keep cursor at the end of the text after toggling
            etPassword.setSelection(etPassword.getText().length());
        });

        btnRegister.setOnClickListener(v -> attemptRegistration());
    }

    /**
     * Reads the form fields, validates them, then calls the backend.
     */
    private void attemptRegistration() {
        // .getText().toString().trim() reads the text and removes leading/trailing spaces
        String username = etUsername.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            etUsername.setError("Username is required");
            etUsername.requestFocus(); // Move cursor to this field
            return; // Stop here, don't send request
        }
        if (username.length() < 5 || username.length() > 20) {
            etUsername.setError("Username must be between 5 and 20 characters");
            etUsername.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
            etEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        if (password.length() < 8 || password.length() > 30) {
            etPassword.setError("Password must be between 8 and 30 characters");
            etPassword.requestFocus();
            return;
        }

        // All validation passed — disable button to prevent double-clicks
        btnRegister.setEnabled(false);
        btnRegister.setText("Registering...");

        RegisterRequest requestBody = new RegisterRequest(username, email, password);

        ApiService apiService = ApiClient.getInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.registerUser(requestBody);

        // 4. .enqueue() sends the request on a BACKGROUND thread (never block the UI thread with network!)
        //    and delivers the result back on the MAIN thread via the Callback
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                btnRegister.setEnabled(true);
                btnRegister.setText("Register");

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String msg = response.body().string();

                        Toast.makeText(RegisterActivity.this,
                                msg,
                                Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Registration failed",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText("Register");

                Toast.makeText(RegisterActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}