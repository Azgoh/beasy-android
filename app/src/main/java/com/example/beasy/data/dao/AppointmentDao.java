package com.example.beasy.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.beasy.data.model.Appointment;

import java.util.List;

/**
 * AppointmentDao — all database operations for the appointments table.
 */
@Dao
public interface AppointmentDao {

    @Insert
    long insertAppointment(Appointment appointment);

    /** Get all appointments for a specific user, newest first */
    @Query("SELECT * FROM appointments WHERE userId = :userId ORDER BY date DESC, time DESC")
    List<Appointment> getAppointmentsForUser(int userId);

    /** Check if a slot is already booked — used to validate before confirming */
    @Query("SELECT * FROM appointments WHERE professionalName = :name AND date = :date AND time = :time LIMIT 1")
    Appointment findExistingBooking(String name, String date, String time);
}