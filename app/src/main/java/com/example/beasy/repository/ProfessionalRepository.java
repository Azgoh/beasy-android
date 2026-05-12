package com.example.beasy.repository;

import com.example.beasy.data.model.Professional;

import java.util.Arrays;
import java.util.List;

/**
 * ProfessionalRepository — holds the single source of truth for the professionals list.
 *
 * WHY: Professional objects are mutable (removeSlot mutates availability).
 * If we create new Professional objects in each Activity, removing a slot
 * in BookingActivity wouldn't affect the list in HomeActivity.
 *
 * By making this a singleton, HomeActivity and BookingActivity both
 * reference the SAME Professional objects — so slot removal is reflected everywhere.
 *
 */
public class ProfessionalRepository {

    private static ProfessionalRepository instance;
    private final List<Professional> professionals;

    private ProfessionalRepository() {
        professionals = Arrays.asList(
                new Professional("Master Sparky Electrics", "Electrical", 5.0f, 128, "Starts $85/hr"),
                new Professional("Elite Flow Plumbing",     "Plumbing",   4.9f, 245, "Starts $95/hr"),
                new Professional("CleanPro Services",       "Cleaning",   4.8f, 310, "Starts $60/hr"),
                new Professional("ArcticAir HVAC",          "HVAC",       4.7f,  98, "Starts $110/hr"),
                new Professional("ColorCraft Painting",     "Painting",   4.6f, 134, "Starts $70/hr"),
                new Professional("GreenThumb Gardening",    "Gardening",  4.8f,  76, "Starts $55/hr")
        );
    }

    public static synchronized ProfessionalRepository getInstance() {
        if (instance == null) instance = new ProfessionalRepository();
        return instance;
    }

    public List<Professional> getAll() { return professionals; }

    /** Finds a professional by name — used by BookingActivity to get the right object */
    public Professional findByName(String name) {
        for (Professional p : professionals) {
            if (p.getName().equals(name)) return p;
        }
        return null;
    }
}