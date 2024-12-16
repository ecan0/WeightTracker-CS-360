package com.example.weighttracker.Data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LoggedWeightDao {
    @Insert
    void insert(LoggedWeight loggedWeight);

    @Query("SELECT * FROM loggedWeights")
    List<LoggedWeight> getAllLoggedWeights();
}