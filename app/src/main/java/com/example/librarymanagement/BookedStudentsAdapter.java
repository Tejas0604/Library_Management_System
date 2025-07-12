package com.example.librarymanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookedStudentsAdapter extends RecyclerView.Adapter<BookedStudentsAdapter.BookingViewHolder> {

    Context context;
    List<Booking> bookingList;

    public BookedStudentsAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booked_student, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        holder.tvName.setText("Name: " + booking.getStudentName());
        holder.tvMobile.setText("Mobile: " + booking.getMobileNumber());
        holder.tvSeat.setText("Seat: " + booking.getSeatNumber());
        holder.tvPlan.setText("Plan: " + booking.getMembershipPlan());
        holder.tvEnd.setText("End Date: " + booking.getEndDate());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            Date endDate = sdf.parse(booking.getEndDate());
            long diff = endDate.getTime() - System.currentTimeMillis();
            long remaining = Math.max(diff / (1000 * 60 * 60 * 24), 0);
            holder.tvRemaining.setText("Remaining Days: " + remaining);
        } catch (Exception e) {
            holder.tvRemaining.setText("Remaining Days: N/A");
        }
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvMobile, tvSeat, tvPlan, tvEnd, tvRemaining;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvMobile = itemView.findViewById(R.id.tvMobile);
            tvSeat = itemView.findViewById(R.id.tvSeat);
            tvPlan = itemView.findViewById(R.id.tvPlan);
            tvEnd = itemView.findViewById(R.id.tvEnd);
            tvRemaining = itemView.findViewById(R.id.tvRemaining);
        }
    }
}
