package com.example.beasy.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beasy.data.AppDatabase;
import com.example.beasy.repository.ProfessionalRepository;
import com.example.beasy.data.model.Appointment;
import com.example.beasy.databinding.ActivityBookingBinding;
import com.example.beasy.data.model.Professional;
import com.example.beasy.ui.adapter.DateAdapter;
import com.example.beasy.ui.adapter.TimeSlotAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * BookingActivity — lets the user pick a date, time slot, and describe their issue.
 * On confirm, saves an AppointmentEntity to Room and removes the slot from the professional.
 *
 * Receives the professional's name via Intent extra "professional_name".
 */
public class BookingActivity extends AppCompatActivity {

    public static final String EXTRA_PROFESSIONAL_NAME = "professional_name";

    private ActivityBookingBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Professional professional;
    private String       selectedDate;
    private String       selectedTime;

    private DateAdapter     dateAdapter;
    private TimeSlotAdapter timeSlotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the professional from the repository using the name passed via Intent
        String proName = getIntent().getStringExtra(EXTRA_PROFESSIONAL_NAME);
        professional   = ProfessionalRepository.getInstance().findByName(proName);

        if (professional == null) {
            Toast.makeText(this, "Professional not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fill the service summary card at the top
        binding.tvProName.setText(professional.getName());
        binding.tvProPrice.setText(professional.getPrice());
        binding.tvProCategory.setText(professional.getCategory());

        // Back button
        binding.btnBack.setOnClickListener(v -> finish());

        setupDates();
        setupConfirmButton();
    }

    /**
     * Builds the next 7 days as selectable date chips.
     * Defaults to selecting today.
     */
    private void setupDates() {
        DateTimeFormatter displayFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();

        List<String> dates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            dates.add(today.plusDays(i).format(displayFmt));
        }

        // Auto-select today
        selectedDate = dates.get(0);

        dateAdapter = new DateAdapter(dates, selectedDate, date -> {
            selectedDate = date;
            selectedTime = null; // reset time when date changes
            setupTimeSlots();
        });

        binding.rvDates.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvDates.setAdapter(dateAdapter);

        // Load time slots for the default selected date
        setupTimeSlots();
    }

    /**
     * Loads the available time slots for the currently selected date.
     * Greyed-out slots aren't shown at all — removed from professional's availability on booking.
     */
    private void setupTimeSlots() {
        List<String> slots = professional.getSlotsForDate(selectedDate);

        timeSlotAdapter = new TimeSlotAdapter(slots, time -> {
            selectedTime = time;
        });

        binding.rvTimeSlots.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        binding.rvTimeSlots.setAdapter(timeSlotAdapter);
    }

    private void setupConfirmButton() {
        binding.btnConfirm.setOnClickListener(v -> {

            // Validate selections
            if (selectedDate == null) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedTime == null) {
                Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
                return;
            }

            String description = binding.etDescription.getText().toString().trim();
            if (description.isEmpty()) {
                binding.etDescription.setError("Please describe the issue");
                binding.etDescription.requestFocus();
                return;
            }

            binding.btnConfirm.setEnabled(false);
            binding.btnConfirm.setText("Confirming...");

            SharedPreferences prefs  = getSharedPreferences("beasy_prefs", MODE_PRIVATE);
            int               userId = prefs.getInt("logged_in_user_id", -1);

            executor.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(this);

                // Double-check the slot isn't already booked in the DB
                Appointment existing = db.appointmentDao()
                        .findExistingBooking(professional.getName(), selectedDate, selectedTime);

                if (existing != null) {
                    runOnUiThread(() -> {
                        binding.btnConfirm.setEnabled(true);
                        binding.btnConfirm.setText("Confirm Appointment →");
                        Toast.makeText(this,
                                "That slot was just booked. Please choose another time.",
                                Toast.LENGTH_LONG).show();
                        // Remove the slot from UI too
                        professional.removeSlot(selectedDate, selectedTime);
                        selectedTime = null;
                        setupTimeSlots();
                    });
                    return;
                }

                // Save the appointment
                Appointment appointment = new Appointment(
                        userId,
                        professional.getName(),
                        professional.getCategory(),
                        professional.getPrice(),
                        selectedDate,
                        selectedTime,
                        description,
                        "UPCOMING"
                );
                db.appointmentDao().insertAppointment(appointment);

                // Remove the slot from the in-memory professional object
                // so it disappears from the UI immediately
                professional.removeSlot(selectedDate, selectedTime);

                runOnUiThread(() -> {
                    Toast.makeText(this, "✅ Appointment confirmed!", Toast.LENGTH_SHORT).show();
                    // Navigate to MyBookingsActivity
                    Intent intent = new Intent(this, MyBookingsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clear back stack to home
                    startActivity(intent);
                    finish();
                });
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        binding = null;
    }
}