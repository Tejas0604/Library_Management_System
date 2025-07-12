package com.example.librarymanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.HistoryViewHolder> {

    private List<Booking> bookingList;

    public BookingHistoryAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tvLab.setText("🏢 Library: " + booking.getLabName());
        holder.tvSeat.setText("💺 Seat: " + booking.getSeatNumber());
        holder.tvPlan.setText("📅 Plan: " + booking.getMembershipPlan());
        holder.tvEndDate.setText("📆 End: " + booking.getEndDate());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvLab, tvSeat, tvPlan, tvEndDate;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLab = itemView.findViewById(R.id.tvLab);
            tvSeat = itemView.findViewById(R.id.tvSeat);
            tvPlan = itemView.findViewById(R.id.tvPlan);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
        }
    }
}
