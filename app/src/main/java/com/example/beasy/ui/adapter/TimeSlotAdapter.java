package com.example.beasy.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beasy.R;

import java.util.List;

/**
 * TimeSlotAdapter — grid of selectable time slot chips.
 * Only shows slots that are still available (removed ones don't appear).
 * Selected slot gets a filled blue background.
 */
public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    public interface OnTimeClickListener {
        void onTimeClick(String time);
    }

    private final List<String>      slots;
    private       String            selectedTime;
    private final OnTimeClickListener listener;

    public TimeSlotAdapter(List<String> slots, OnTimeClickListener listener) {
        this.slots    = slots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String time       = slots.get(position);
        boolean isSelected = time.equals(selectedTime);

        holder.tvTime.setText(time);
        holder.itemView.setBackgroundResource(
                isSelected ? R.drawable.time_selected_bg : R.drawable.time_unselected_bg
        );
        holder.tvTime.setTextColor(holder.itemView.getContext().getColor(
                isSelected ? R.color.white : R.color.text_dark
        ));

        holder.itemView.setOnClickListener(v -> {
            selectedTime = time;
            notifyDataSetChanged();
            listener.onTimeClick(time);
        });
    }

    @Override
    public int getItemCount() { return slots.size(); }

    static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;

        TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTimeSlot);
        }
    }
}