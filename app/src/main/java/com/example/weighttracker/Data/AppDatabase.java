package com.example.weighttracker.Data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

// Define the database configuration and entities
@Database(entities = {User.class, LoggedWeight.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    // Abstract methods to get DAOs
    public abstract UserDao userDao();
    public abstract LoggedWeightDao loggedWeightDao();

    // Singleton instance of the database
    private static volatile AppDatabase INSTANCE;

    // Method to get the singleton instance of the database
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Build the database instance
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "weight_tracker_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}