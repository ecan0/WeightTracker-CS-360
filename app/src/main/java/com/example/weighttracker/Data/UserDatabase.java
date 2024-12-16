package com.example.weighttracker.Data;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

// Define the database configuration and entities
@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    // Singleton instance of the database
    private static UserDatabase instance;

    // Abstract method to get the UserDao
    public abstract UserDao userDao();

    // Method to get the singleton instance of the database
    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            // Build the database instance
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class, "user_database")
                    .fallbackToDestructiveMigration() // Handle migrations by recreating the database
                    .build();
        }
        return instance;
    }
}