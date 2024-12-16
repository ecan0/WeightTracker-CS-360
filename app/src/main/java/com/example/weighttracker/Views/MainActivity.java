package com.example.weighttracker.Views;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.weighttracker.Controller.LoggedWeightAdapter;
import com.example.weighttracker.Data.LoggedWeight;
import com.example.weighttracker.Data.LoggedWeightDatabase;
import com.example.weighttracker.Data.User;
import com.example.weighttracker.Data.UserDatabase;
import com.example.weighttracker.R;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//FINAL PROJECT SUBMISSION: Weight Tracker App
//Eric Candela
//Bachelor of Science in Computer Science, SNHU
//CS 360: Mobile Architecture and Programming
//December 15, 2024

public class MainActivity extends AppCompatActivity {

    // Request code for SMS permission
    private static final int SMS_PERMISSION_REQUEST_CODE = 1001;

    // UI elements
    private TextView goalWeightText;
    private LoggedWeightAdapter adapter;

    // Database instances
    private UserDatabase userDatabase;
    private LoggedWeightDatabase loggedWeightDatabase;

    // User data
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        goalWeightText = findViewById(R.id.goalWeightText);

        // Initialize database instances
        userDatabase = UserDatabase.getInstance(this);
        loggedWeightDatabase = LoggedWeightDatabase.getDatabase(this);

        // Initialize RecyclerView for displaying logged weights
        RecyclerView recyclerView = findViewById(R.id.dataRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LoggedWeightAdapter(new ArrayList<>()); // Initialize with an empty list
        recyclerView.setAdapter(adapter);

        // Fetch logged weights and update UI
        fetchLoggedWeightsAndUpdateUI();

        // Fetch user data and update UI
        new Thread(() -> {
            user = userDatabase.userDao().getUserById(1); // Assuming user ID is 1
            runOnUiThread(() -> {
                if (user != null) {
                    if (user.goalWeight == 0) {
                        promptForGoalWeight(); // Prompt user to set goal weight if not set
                    } else {
                        updateGoalWeightText(); // Update goal weight text view
                    }
                }
            });
        }).start();

        // Set button click listeners
        ImageButton editGoalWeightButton = findViewById(R.id.editGoalWeightButton);
        editGoalWeightButton.setOnClickListener(v -> promptForGoalWeight());

        ImageButton notificationButton = findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(v -> requestSmsPermission());

        ImageButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> showWeightDialog(null));

        ImageButton editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {
            LoggedWeight selectedWeight = adapter.getSelectedLoggedWeight();
            if (selectedWeight != null) {
                showWeightDialog(selectedWeight);
            } else {
                Toast.makeText(this, "Please select an entry to edit.", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            LoggedWeight selectedWeight = adapter.getSelectedLoggedWeight();
            if (selectedWeight != null) {
                deleteLoggedWeight(selectedWeight);
            } else {
                Toast.makeText(this, "Please select an entry to delete.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch logged weights from database and update UI
    private void fetchLoggedWeightsAndUpdateUI() {
        new Thread(() -> {
            List<LoggedWeight> loggedWeights = fetchLoggedWeights();
            for (LoggedWeight weight : loggedWeights) {
                weight.goalMet = (weight.weight == user.goalWeight); // Check if goal weight is met
            }
            runOnUiThread(() -> adapter.updateData(loggedWeights));
        }).start();
    }

    // Fetch logged weights from database
    private List<LoggedWeight> fetchLoggedWeights() {
        List<LoggedWeight> loggedWeights = loggedWeightDatabase.loggedWeightDao().getAllLoggedWeights();
        return loggedWeights != null ? loggedWeights : new ArrayList<>();
    }

    // Show dialog to add or edit weight
    private void showWeightDialog(LoggedWeight loggedWeight) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(loggedWeight == null ? "Add Weight" : "Edit Weight");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        if (loggedWeight != null) {
            input.setText(String.valueOf(loggedWeight.weight));
        }
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            int weight = Integer.parseInt(input.getText().toString());
            if (weight < 100 || weight > 800) {
                Toast.makeText(this, "Weight must be between 100 and 800 lbs.", Toast.LENGTH_LONG).show();
            } else {
                if (loggedWeight == null) {
                    addLoggedWeight(weight); // Add new weight
                } else {
                    loggedWeight.weight = weight;
                    loggedWeight.goalMet = weight <= user.goalWeight;
                    updateLoggedWeight(loggedWeight); // Update existing weight
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Add new logged weight to database
    private void addLoggedWeight(int weight) {
        new Thread(() -> {
            LoggedWeight newWeight = new LoggedWeight();
            newWeight.date = LocalDate.now().toString(); // Set the current date
            newWeight.weight = weight;
            newWeight.goalMet = (weight == user.goalWeight); // Check if goal weight is met
            loggedWeightDatabase.loggedWeightDao().insert(newWeight); // Insert into database
            runOnUiThread(() -> {
                fetchLoggedWeightsAndUpdateUI(); // Fetch and update UI
                if (newWeight.weight == user.goalWeight) {
                    sendSmsNotification("Congratulations! You've reached your goal weight.");
                }
            });
        }).start();
    }

    // Update existing logged weight in database
    private void updateLoggedWeight(LoggedWeight loggedWeight) {
        new Thread(() -> {
            loggedWeight.goalMet = (loggedWeight.weight == user.goalWeight); // Check if goal weight is met
            loggedWeightDatabase.loggedWeightDao().update(loggedWeight); // Update in database
            runOnUiThread(() -> {
                fetchLoggedWeightsAndUpdateUI(); // Fetch and update UI
                if (loggedWeight.weight == user.goalWeight) {
                    sendSmsNotification("Congratulations! You've reached your goal weight.");
                }
            });
        }).start();
    }

    // Delete logged weight from database
    private void deleteLoggedWeight(LoggedWeight loggedWeight) {
        new Thread(() -> {
            loggedWeightDatabase.loggedWeightDao().delete(loggedWeight);
            runOnUiThread(this::fetchLoggedWeightsAndUpdateUI);
        }).start();
    }

    // Prompt user to enter goal weight
    private void promptForGoalWeight() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Goal Weight");

        final EditText input = new EditText(this);
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            int goalWeight = Integer.parseInt(input.getText().toString());
            if (goalWeight < 100 || goalWeight > 800) {
                Toast.makeText(this, "Goal weight must be between 100 and 800 lbs.", Toast.LENGTH_LONG).show();
            } else {
                user.goalWeight = goalWeight;
                new Thread(() -> {
                    userDatabase.userDao().update(user);
                    runOnUiThread(this::updateGoalWeightText);
                }).start();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Update goal weight text view
    private void updateGoalWeightText() {
        String message = "Hello, " + user.username + ", your goal weight is " + user.goalWeight + " lbs.";
        goalWeightText.setText(message);
    }

    // Request SMS permissions
    private void requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        } else {
            Toast.makeText(this, "SMS notifications enabled.", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS notifications enabled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Send SMS notification
    private void sendSmsNotification(String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            // This is not used because we're not sending actual SMS over the network.
            smsManager.sendTextMessage("1234567890", null, message, null, null);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            // There's no permission to send SMS, so we can't send the notification
        }
    }
}