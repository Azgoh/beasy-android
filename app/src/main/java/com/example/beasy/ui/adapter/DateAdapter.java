package com.example.beasy.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beasy.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * DateAdapter — horizontal list of date chips for the booking screen.
 * Shows month, day number, and day name (MON/TUE etc).
 * Selected date gets a blue border highlight.
 */
public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    public interface OnDateClickListener {
        void onDateClick(String date); // returns "yyyy-MM-dd"
    }

    private final List<String>      dates;
    private       String            selectedDate;
    private final OnDateClickListener listener;

    private static final DateTimeFormatter INPUT_FMT   = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FMT   = DateTimeFormatter.ofPattern("MMM").withLocale(java.util.Locale.ENGLISH);
    private static final DateTimeFormatter DAY_NAME_FMT = DateTimeFormatter.ofPattern("EEE").withLocale(java.util.Locale.ENGLISH);

    public DateAdapter(List<String> dates, String selectedDate, OnDateClickListener listener) {
        this.dates        = dates;
        this.selectedDate = selectedDate;
        this.listener     = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        String    dateStr = dates.get(position);
        LocalDate date    = LocalDate.parse(dateStr, INPUT_FMT);

        holder.tvMonth.setText(MONTH_FMT.format(date).toUpperCase());
        holder.tvDay.setText(String.valueOf(date.getDayOfMonth()));
        holder.tvDayName.setText(DAY_NAME_FMT.format(date).toUpperCase());

        // Highlight selected date
        boolean isSelected = dateStr.equals(selectedDate);
        holder.itemView.setBackgroundResource(
                isSelected ? R.drawable.date_selected_bg : R.drawable.date_unselected_bg
        );
        holder.tvMonth.setTextColor(holder.itemView.getContext().getColor(
                isSelected ? R.color.primary_blue : R.color.text_grey));
        holder.tvDay.setTextColor(holder.itemView.getContext().getColor(
                isSelected ? R.color.primary_blue : R.color.text_dark));
        holder.tvDayName.setTextColor(holder.itemView.getContext().getColor(
                isSelected ? R.color.primary_blue : R.color.text_grey));

        holder.itemView.setOnClickListener(v -> {
            selectedDate = dateStr;
            notifyDataSetChanged(); // refresh all items to update highlight
            listener.onDateClick(dateStr);
        });
    }

    @Override
    public int getItemCount() { return dates.size(); }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth, tvDay, tvDayName;

        DateViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMonth   = itemView.findViewById(R.id.tvDateMonth);
            tvDay     = itemView.findViewById(R.id.tvDateDay);
            tvDayName = itemView.findViewById(R.id.tvDateDayName);
        }
    }
}