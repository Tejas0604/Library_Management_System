package com.example.librarymanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private TextView welcomeText, libraryName, seatNumber, plan, remainingDays, endDate;
    private Button btnCancelBooking;
    private DatabaseReference bookingsRef, seatRef;
    private String userId;
    private Booking currentBooking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Views
        welcomeText = view.findViewById(R.id.welcomeText);
        libraryName = view.findViewById(R.id.libraryName);
        seatNumber = view.findViewById(R.id.seatNumber);
        plan = view.findViewById(R.id.plan);
        remainingDays = view.findViewById(R.id.remainingDays);
        endDate = view.findViewById(R.id.endDate);
        btnCancelBooking = view.findViewById(R.id.btnCancelBooking);

        // New buttons
        Button btnHistory = view.findViewById(R.id.btnHistory);
        Button btnGoToPayment = view.findViewById(R.id.btnGoToPayment);

        // Load booking data
        userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            bookingsRef = FirebaseDatabase.getInstance().getReference("bookings").child(userId);
            loadBookingDetails();
        }

        // Cancel booking listener
        btnCancelBooking.setOnClickListener(v -> cancelBooking());

        // Booking History button listener
        btnHistory.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Booking History clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Booking History screen here
        });

        // Make Payment button listener
        btnGoToPayment.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Make Payment clicked", Toast.LENGTH_SHORT).show();
            // TODO: Navigate to Payment screen here
        });

        return view;
    }

    private void loadBookingDetails() {
        bookingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Booking booking = snapshot.getValue(Booking.class);
                    currentBooking = booking;
                    if (booking != null) {
                        welcomeText.setText("👋 Welcome, " + booking.getStudentName());
                        libraryName.setText("🏢 Library: " + booking.getLabName());
                        seatNumber.setText("💺 Seat No: " + booking.getSeatNumber());
                        plan.setText("📅 Plan: " + booking.getMembershipPlan());
                        endDate.setText("📆 End Date: " + booking.getEndDate());
                        int daysLeft = calculateDaysLeft(booking.getEndDate());
                        remainingDays.setText("⏳ Days Left: " + daysLeft);

                        // Set seatRef path for updating seat status on cancel
                        seatRef = FirebaseDatabase.getInstance()
                                .getReference("lab_info")
                                .child(booking.getLabName())
                                .child("seatStatus")
                                .child(String.valueOf(Integer.parseInt(booking.getSeatNumber()) - 1));
                    }
                } else {
                    welcomeText.setText("👋 Welcome");
                    libraryName.setText("No active booking.");
                    seatNumber.setText("");
                    plan.setText("");
                    remainingDays.setText("");
                    endDate.setText("");
                    btnCancelBooking.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch booking.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int calculateDaysLeft(String endDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date endDate = sdf.parse(endDateStr);
            Date today = new Date();
            long diff = endDate.getTime() - today.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            return 0;
        }
    }

    private void cancelBooking() {
        if (currentBooking == null) return;

        // Move booking to history
        DatabaseReference historyRef = FirebaseDatabase.getInstance()
                .getReference("history")
                .child(userId)
                .push();

        historyRef.setValue(currentBooking).addOnCompleteListener(historyTask -> {
            if (historyTask.isSuccessful()) {
                // Unbook the seat
                if (seatRef != null) {
                    seatRef.child("isBooked").setValue(false);
                }

                // Remove current booking
                bookingsRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Booking cancelled and moved to history.", Toast.LENGTH_SHORT).show();

                        // Clear UI
                        welcomeText.setText("👋 Welcome");
                        libraryName.setText("No active booking.");
                        seatNumber.setText("");
                        plan.setText("");
                        remainingDays.setText("");
                        endDate.setText("");
                        btnCancelBooking.setVisibility(View.GONE);
                    }
                });
            } else {
                Toast.makeText(getContext(), "Failed to move to history.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
