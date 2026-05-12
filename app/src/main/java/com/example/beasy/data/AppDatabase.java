package com.example.beasy.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.beasy.data.dao.AppointmentDao;
import com.example.beasy.data.dao.UserDao;
import com.example.beasy.data.model.Appointment;
import com.example.beasy.data.model.UserEntity;

@Database(
        entities = { UserEntity.class, Appointment.class },
        version  = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao        userDao();
    public abstract AppointmentDao appointmentDao();

    private static volatile AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "beasy_database"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}