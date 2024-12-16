package com.example.weighttracker.Data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

// Define the database with LoggedWeight entity
@Database(entities = {LoggedWeight.class}, version = 1)
public abstract class LoggedWeightDatabase extends RoomDatabase {
    private static volatile LoggedWeightDatabase INSTANCE;

    // Abstract method to get the DAO
    public abstract LoggedWeightDao loggedWeightDao();

    // Singleton pattern to get the database instance
    public static LoggedWeightDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LoggedWeightDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    LoggedWeightDatabase.class, "logged_weight_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}