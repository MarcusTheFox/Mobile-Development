package ru.mirea.bublikov.employeedb;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    public static App instance;

    private AppDatabase database;

    private static final String DATABASE_NAME = "superheroes_database";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }

    public boolean deleteDatabase() {
        return getApplicationContext().deleteDatabase(DATABASE_NAME);
    }
}
