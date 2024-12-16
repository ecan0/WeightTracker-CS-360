package com.example.weighttracker.Data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "loggedWeights")
public class LoggedWeight {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String date; // Use String for simplicity, you can use Date type with TypeConverters
    public int weight;
    public boolean goalMet;
}