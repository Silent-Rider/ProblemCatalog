package com.example.problem_catalog.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.problem_catalog.BuildConfig;
import com.example.problem_catalog.model.entities.Problem;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Database(entities = {Problem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    public abstract ProblemDao problemDao();
    public static AppDatabase getInstance(@ApplicationContext Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, BuildConfig.DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
