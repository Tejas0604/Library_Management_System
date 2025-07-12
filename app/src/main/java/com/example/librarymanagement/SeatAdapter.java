package com.example.librarymanagement;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class
    SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {

    private Context context;
    private List<Seat> seatList;

    public SeatAdapter(Context context, List<Seat> seatList) {
        this.context = context;
        this.seatList = seatList;
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.seatTextView.setText("Seat " + (position + 1));

        if (seat.isBooked()) {
            holder.seatTextView.setBackgroundColor(Color.parseColor("#FF6B6B")); // red
        } else {
            holder.seatTextView.setBackgroundColor(Color.parseColor("#7ED957")); // green
        }

        holder.seatTextView.setOnClickListener(v -> {
            if (seat.isBooked()) {
                Toast.makeText(context, "Seat already booked", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Available! Seat " + (position + 1), Toast.LENGTH_SHORT).show();
                // You can open booking screen here
            }
        });
    }

    @Override
    public int getItemCount() {
        return seatList.size();
    }

    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        TextView seatTextView;

        public SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            seatTextView = itemView.findViewById(R.id.seatTextView);
        }
    }
}
