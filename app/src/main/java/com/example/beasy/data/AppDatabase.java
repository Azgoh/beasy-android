package com.example.beasy.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.beasy.data.dao.UserDao;
import com.example.beasy.data.model.UserEntity;

@Database(
        entities = { UserEntity.class },
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    private static volatile AppDatabase instance;

    /**
     * Get the database instance. Creates it if it doesn't exist yet.
     *
     * "volatile" + "synchronized" makes this thread-safe.
     * Context is needed to know where to store the SQLite file on the device.
     */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(), // Always use app context, not activity
                            AppDatabase.class,
                            "beasy_database"                // The .db file name on disk
                    ).build();
                }
            }
        }
        return instance;
    }
}