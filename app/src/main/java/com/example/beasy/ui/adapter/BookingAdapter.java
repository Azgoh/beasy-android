package com.example.beasy.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beasy.R;
import com.example.beasy.data.model.Appointment;

import java.util.List;

/**
 * BookingAdapter — drives the list of booked appointments in MyBookingsActivity.
 */
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<Appointment> appointments;

    public BookingAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Appointment a = appointments.get(position);
        holder.tvProName.setText(a.professionalName);
        holder.tvCategory.setText(a.category);
        holder.tvDateTime.setText(a.date + " at " + a.time);
        holder.tvPrice.setText(a.price);
        holder.tvDescription.setText(a.description);
        holder.tvStatus.setText(a.status);

        // Color the status badge
        int color = a.status.equals("UPCOMING")
                ? holder.itemView.getContext().getColor(R.color.primary_blue)
                : holder.itemView.getContext().getColor(R.color.text_grey);
        holder.tvStatus.setTextColor(color);
    }

    @Override
    public int getItemCount() { return appointments.size(); }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvProName, tvCategory, tvDateTime, tvPrice, tvDescription, tvStatus;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProName    = itemView.findViewById(R.id.tvBookingProName);
            tvCategory   = itemView.findViewById(R.id.tvBookingCategory);
            tvDateTime   = itemView.findViewById(R.id.tvBookingDateTime);
            tvPrice      = itemView.findViewById(R.id.tvBookingPrice);
            tvDescription = itemView.findViewById(R.id.tvBookingDescription);
            tvStatus     = itemView.findViewById(R.id.tvBookingStatus);
        }
    }
}