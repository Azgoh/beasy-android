package com.example.beasy.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.beasy.data.AppDatabase;
import com.example.beasy.data.model.Appointment;
import com.example.beasy.databinding.ActivityMyBookingsBinding;
import com.example.beasy.ui.adapter.BookingAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MyBookingsActivity — shows all appointments for the logged-in user.
 * Fetches from Room on a background thread, then displays in a RecyclerView.
 */
public class MyBookingsActivity extends AppCompatActivity {

    private ActivityMyBookingsBinding binding;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBookingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnBack.setOnClickListener(v -> finish());

        loadBookings();
    }

    private void loadBookings() {
        SharedPreferences prefs  = getSharedPreferences("beasy_prefs", MODE_PRIVATE);
        int               userId = prefs.getInt("logged_in_user_id", -1);

        executor.execute(() -> {
            List<Appointment> appointments =
                    AppDatabase.getInstance(this).appointmentDao().getAppointmentsForUser(userId);

            runOnUiThread(() -> {
                if (appointments.isEmpty()) {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.rvBookings.setVisibility(View.GONE);
                } else {
                    binding.tvEmpty.setVisibility(View.GONE);
                    binding.rvBookings.setVisibility(View.VISIBLE);
                    BookingAdapter adapter = new BookingAdapter(appointments);
                    binding.rvBookings.setLayoutManager(new LinearLayoutManager(this));
                    binding.rvBookings.setAdapter(adapter);
                }
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