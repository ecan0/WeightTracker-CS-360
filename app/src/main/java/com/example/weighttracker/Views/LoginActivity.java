package com.example.weighttracker.Views;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;
import com.example.weighttracker.Data.User;
import com.example.weighttracker.Data.UserDao;
import com.example.weighttracker.Data.UserDatabase;
import com.example.weighttracker.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    // UI elements
    EditText username;
    EditText password;
    Button loginButton;
    Button registerButton;

    // Database access object
    UserDao userDao;

    // Executor service for background tasks
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Initialize UserDao and ExecutorService
        userDao = UserDatabase.getInstance(this).userDao();
        executorService = Executors.newSingleThreadExecutor();

        // Set login button click listener
        loginButton.setOnClickListener(view -> {
            String usernameText = username.getText().toString().trim();
            String passwordText = password.getText().toString().trim();

            // Check if username and password are empty
            if (usernameText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Username and password cannot be empty.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Execute login task in background
            executorService.execute(() -> {
                User user = userDao.getUser(usernameText, passwordText);
                runOnUiThread(() -> {
                    if (user != null) {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login Failed! Invalid username or password.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        // Set register button click listener
        registerButton.setOnClickListener(this::registerHelper);
    }

    // Helper method for user registration
    public void registerHelper(View view) {
        String usernameText = username.getText().toString().trim();
        String passwordText = password.getText().toString().trim();

        // Check if username and password are empty
        if (usernameText.isEmpty() || passwordText.isEmpty()) {
            showAlertDialog("Error", "Username and password cannot be empty.");
            return;
        }

        // Execute registration task in background
        executorService.execute(() -> {
            User existingUser = userDao.getUser(usernameText, passwordText);
            if (existingUser != null) {
                runOnUiThread(() -> showAlertDialog("Error", "User already exists."));
            } else {
                User newUser = new User();
                newUser.username = usernameText;
                newUser.password = passwordText;
                userDao.insert(newUser);
                runOnUiThread(() -> showAlertDialog("Success", "User registered successfully."));
            }
        });
    }

    // Method to show alert dialog
    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}