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

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 1001;
    private TextView goalWeightText;
    private UserDatabase userDatabase;
    private User user;
    private LoggedWeightAdapter adapter;
    private LoggedWeightDatabase loggedWeightDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goalWeightText = findViewById(R.id.goalWeightText);
        userDatabase = UserDatabase.getInstance(this);
        loggedWeightDatabase = LoggedWeightDatabase.getDatabase(this);

        // Initialize RecyclerView for LoggedWeight
        RecyclerView recyclerView = findViewById(R.id.dataRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LoggedWeightAdapter(new ArrayList<>()); // Initialize with an empty list
        recyclerView.setAdapter(adapter);

        fetchLoggedWeightsAndUpdateUI();

        // Fetch user data and update UI
        new Thread(() -> {
            user = userDatabase.userDao().getUserById(1); // Assuming user ID is 1
            runOnUiThread(() -> {
                if (user != null) {
                    if (user.goalWeight == 0) {
                        promptForGoalWeight();
                    } else {
                        updateGoalWeightText();
                    }
                }
            });
        }).start();

        // Set button click listeners
        ImageButton editGoalWeightButton = findViewById(R.id.editGoalWeightButton);
        editGoalWeightButton.setOnClickListener(v -> promptForGoalWeight());

        ImageButton notificationButton = findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(v -> sendNotificationSMS());

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

    // Fetch logged weights and update UI
    private void fetchLoggedWeightsAndUpdateUI() {
        new Thread(() -> {
            List<LoggedWeight> loggedWeights = fetchLoggedWeights();
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
                    addLoggedWeight(weight);
                } else {
                    loggedWeight.weight = weight;
                    loggedWeight.goalMet = weight <= user.goalWeight;
                    updateLoggedWeight(loggedWeight);
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Add new logged weight
    private void addLoggedWeight(int weight) {
        LoggedWeight newWeight = new LoggedWeight();
        newWeight.date = LocalDate.now().toString(); // Set the current date
        newWeight.weight = weight;
        newWeight.goalMet = weight <= user.goalWeight; // Set goalMet based on user goal weight

        new Thread(() -> {
            loggedWeightDatabase.loggedWeightDao().insert(newWeight);
            runOnUiThread(this::fetchLoggedWeightsAndUpdateUI);
        }).start();
    }

    // Update existing logged weight
    private void updateLoggedWeight(LoggedWeight loggedWeight) {
        new Thread(() -> {
            loggedWeightDatabase.loggedWeightDao().update(loggedWeight);
            runOnUiThread(this::fetchLoggedWeightsAndUpdateUI);
        }).start();
    }

    // Delete logged weight
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

    // Send notification SMS if goal weight is met
    private void sendNotificationSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_REQUEST_CODE
            );
        } else {
            sendSMSMessage();
        }
    }

    // Send SMS message
    private void sendSMSMessage() {
        if (user != null && user.goalWeight > 0) {
            List<LoggedWeight> loggedWeights = fetchLoggedWeights();
            for (LoggedWeight weight : loggedWeights) {
                if (weight.goalMet) {
                    String message = "Congratulations! You have met your goal weight of " + user.goalWeight + " lbs.";
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("1234567890", null, message, null, null);
                        Toast.makeText(this, "Notification sent.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to send SMS. Check permissions: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMSMessage();
            } else {
                Toast.makeText(this, "SMS permission denied. Cannot send notifications.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}