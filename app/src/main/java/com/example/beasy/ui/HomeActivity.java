package com.example.beasy.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.beasy.databinding.ActivityHomeBinding;
import com.example.beasy.data.model.Category;
import com.example.beasy.data.model.Professional;
import com.example.beasy.repository.ProfessionalRepository;
import com.example.beasy.ui.adapter.CategoryAdapter;
import com.example.beasy.ui.adapter.ProfessionalAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    private List<Category>     allCategories;
    private List<Professional> allProfessionals;

    private CategoryAdapter     categoryAdapter;
    private ProfessionalAdapter professionalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences prefs = getSharedPreferences("beasy_prefs", MODE_PRIVATE);
        String username = prefs.getString("logged_in_username", "there");
        binding.tvGreeting.setText("Hi, " + username + " 👋");

        binding.btnMyBookings.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyBookingsActivity.class);
            startActivity(intent);
        });

        setupData();
        setupCategories();
        setupProfessionals();
        setupSearch();
    }

    private void setupData() {
        allCategories = Arrays.asList(
                new Category("Plumbing",   "🔧"),
                new Category("Electrical", "⚡"),
                new Category("Cleaning",   "🧹"),
                new Category("HVAC",       "❄️"),
                new Category("Painting",   "🖌️"),
                new Category("Gardening",  "🌿")
        );

        // Load professionals from repository
        allProfessionals = ProfessionalRepository
                .getInstance()
                .getAll();
    }

    private void setupCategories() {
        // Tapping a category chip puts its name into the search box → triggers filtering
        categoryAdapter = new CategoryAdapter(allCategories, category ->
                binding.etSearch.setText(category.getName())
        );
        binding.rvCategories.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rvCategories.setAdapter(categoryAdapter);
        binding.rvCategories.setNestedScrollingEnabled(false);
    }

    private void setupProfessionals() {
        professionalAdapter = new ProfessionalAdapter(allProfessionals, pro -> {
            Intent intent = new Intent(HomeActivity.this, BookingActivity.class);

            intent.putExtra(
                    BookingActivity.EXTRA_PROFESSIONAL_NAME,
                    pro.getName()
            );

            startActivity(intent);
        });
        binding.rvProfessionals.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        binding.rvProfessionals.setAdapter(professionalAdapter);
        binding.rvProfessionals.setNestedScrollingEnabled(false);
    }

    private void setupSearch() {
        // Hide X button initially
        binding.ivClearSearch.setVisibility(View.GONE);

        // Show/hide X as user types, and filter
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Show X only when there's text
                binding.ivClearSearch.setVisibility(
                        s.length() > 0 ? View.VISIBLE : View.GONE
                );
                filterByQuery(s.toString().trim());
            }
        });

        // X button clears the search box → TextWatcher fires → resets lists
        binding.ivClearSearch.setOnClickListener(v -> {
            binding.etSearch.setText("");
            binding.etSearch.clearFocus();
        });
    }

    /**
     * Filters categories and professionals by the query string.
     * Empty query resets both lists back to full.
     */
    private void filterByQuery(String query) {
        if (query.isEmpty()) {
            categoryAdapter.updateList(allCategories);
            professionalAdapter.updateList(allProfessionals);
            return;
        }

        String lower = query.toLowerCase();

        List<Category> filteredCategories = allCategories.stream()
                .filter(c -> c.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());

        // Match professionals by category name OR professional name
        List<Professional> filteredProfessionals = allProfessionals.stream()
                .filter(p -> p.getCategory().toLowerCase().contains(lower)
                        || p.getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());

        categoryAdapter.updateList(filteredCategories);
        professionalAdapter.updateList(filteredProfessionals);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}