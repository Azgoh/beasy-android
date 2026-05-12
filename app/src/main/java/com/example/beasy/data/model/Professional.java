package com.example.beasy.data.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Professional — hardcoded data model including availability slots.
 *
 * availability: Map<dateString, List<timeString>>
 * dateString:   "yyyy-MM-dd"  e.g. "2026-05-13"
 * timeString:   "09:00 AM"
 *
 * Slots are generated for the next 7 days relative to today,
 * so they're always current without hardcoding specific dates.
 */
public class Professional {

    private final String name;
    private final String category;
    private final float  rating;
    private final int    reviewCount;
    private final String price;

    // Map of date → mutable list of available times
    private final Map<String, List<String>> availability;

    public Professional(String name, String category, float rating, int reviewCount, String price) {
        this.name         = name;
        this.category     = category;
        this.rating       = rating;
        this.reviewCount  = reviewCount;
        this.price        = price;
        this.availability = generateAvailability();
    }

    /**
     * Builds a fresh set of time slots for the next 7 days.
     * Each day gets its own independent mutable list.
     */
    private Map<String, List<String>> generateAvailability() {
        Map<String, List<String>> map = new HashMap<>();
        DateTimeFormatter fmt   = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate         today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            String date = today.plusDays(i).format(fmt);
            map.put(date, new ArrayList<>(Arrays.asList(
                    "09:00 AM", "11:30 AM", "01:00 PM",
                    "02:30 PM", "04:00 PM", "05:30 PM"
            )));
        }
        return map;
    }

    /**
     * Removes a booked slot so it can't be selected again this session.
     * Called by BookingActivity after a confirmed appointment.
     */
    public void removeSlot(String date, String time) {
        List<String> slots = availability.get(date);
        if (slots != null) slots.remove(time);
    }

    public List<String> getSlotsForDate(String date) {
        List<String> slots = availability.get(date);
        return slots != null ? slots : new ArrayList<>();
    }

    public String getName()        { return name;        }
    public String getCategory()    { return category;    }
    public float  getRating()      { return rating;      }
    public int    getReviewCount() { return reviewCount; }
    public String getPrice()       { return price;       }
}