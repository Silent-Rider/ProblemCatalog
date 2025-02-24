package com.example.problem_catalog.model;

import androidx.room.*;
import com.example.problem_catalog.model.entities.Problem;
import java.util.List;

@Dao
public interface ProblemDao {
    @Query("DELETE FROM problems")
    void clear();

    @Insert
    void insertAll(List<Problem> problems);

    @Query("SELECT * FROM problems")
    List<Problem> findAll();

    @Transaction
    default void updateProblems(List<Problem> problems) {
        clear();
        insertAll(problems);
    }
}
