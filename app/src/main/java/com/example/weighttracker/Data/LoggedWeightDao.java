package com.example.weighttracker.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LoggedWeightDao {
    @Insert
    void insert(LoggedWeight loggedWeight);

    @Update
    void update(LoggedWeight loggedWeight);

    @Delete
    void delete(LoggedWeight loggedWeight);

    @Query("SELECT * FROM loggedWeights")
    List<LoggedWeight> getAllLoggedWeights();
}