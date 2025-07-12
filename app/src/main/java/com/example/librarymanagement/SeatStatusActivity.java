package com.example.librarymanagement;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SeatStatusActivity extends AppCompatActivity {

    GridLayout seatGrid;
    String labId;
    DatabaseReference seatRef;
    int totalSeats;
    int selectedSeat = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_status);

        seatGrid = findViewById(R.id.seatGrid);
        labId = getIntent().getStringExtra("labId");

        if (labId == null || labId.isEmpty()) {
            Toast.makeText(this, "Lab ID not received!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        seatRef = FirebaseDatabase.getInstance().getReference("lab_info").child(labId);

        seatRef.child("seats").get().addOnSuccessListener(snapshot -> {
            totalSeats = snapshot.getValue(Integer.class) != null ? snapshot.getValue(Integer.class) : 0;
            loadSeats();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error loading seat count", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadSeats() {
        seatRef.child("seatStatus").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seatGrid.removeAllViews();
                for (int i = 0; i < totalSeats; i++) {
                    boolean isBooked = snapshot.child(String.valueOf(i)).child("isBooked").getValue(Boolean.class) != null &&
                            snapshot.child(String.valueOf(i)).child("isBooked").getValue(Boolean.class);

                    Button seatButton = new Button(SeatStatusActivity.this);
                    seatButton.setText("Seat " + (i + 1));
                    seatButton.setBackgroundColor(isBooked ? Color.RED : Color.GREEN);
                    seatButton.setTextColor(Color.WHITE);

                    GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                    params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                    params.setMargins(10, 10, 10, 10);
                    seatButton.setLayoutParams(params);

                    final int seatNumber = i;
                    seatButton.setOnClickListener(v -> {
                        if (isBooked) {
                            Toast.makeText(SeatStatusActivity.this, "Seat already allocated!", Toast.LENGTH_SHORT).show();
                        } else {
                            selectedSeat = seatNumber;
                            showBookingDialog();
                        }
                    });

                    seatGrid.addView(seatButton);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SeatStatusActivity.this, "Failed to load seat data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBookingDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_booking_form, null);

        EditText etName = view.findViewById(R.id.etName);
        EditText etMobile = view.findViewById(R.id.etMobile);
        EditText etArrivalTime = view.findViewById(R.id.etArrivalTime);
        EditText etExitTime = view.findViewById(R.id.etExitTime);
        Spinner spinnerPlan = view.findViewById(R.id.spinnerPlan);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new String[]{"1 Month", "3 Months", "6 Months", "1 Year"}
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlan.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("Book", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                String name = etName.getText().toString().trim();
                String mobile = etMobile.getText().toString().trim();
                String arrival = etArrivalTime.getText().toString().trim();
                String exit = etExitTime.getText().toString().trim();
                String plan = spinnerPlan.getSelectedItem() != null ? spinnerPlan.getSelectedItem().toString() : "";

                if (name.isEmpty() || mobile.isEmpty() || arrival.isEmpty() || exit.isEmpty() || plan.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mobile.length() != 10) {
                    Toast.makeText(this, "Enter valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkDuplicateBooking(name, mobile, arrival, exit, plan, dialog);
            });
        });

        dialog.show();
    }

    private void checkDuplicateBooking(String name, String mobile, String arrival, String exit, String plan, AlertDialog dialog) {
        String userId = "user123";
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bookings");
        userRef.child(labId + "_seat_" + selectedSeat).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(SeatStatusActivity.this, "You have already booked this seat!", Toast.LENGTH_SHORT).show();
                } else {
                    bookSelectedSeat(plan, name, mobile, arrival, exit);
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SeatStatusActivity.this, "Error checking booking", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bookSelectedSeat(String plan, String name, String mobile, String arrivalTime, String exitTime) {
        if (selectedSeat == -1) return;

        seatRef.child("name").get().addOnSuccessListener(snapshot -> {
            String actualLabName = snapshot.exists() ? snapshot.getValue(String.class) : "Unknown Lab";

            long currentTimeMillis = System.currentTimeMillis();
            long durationMillis;

            switch (plan) {
                case "3 Months": durationMillis = 90L * 24 * 60 * 60 * 1000; break;
                case "6 Months": durationMillis = 180L * 24 * 60 * 60 * 1000; break;
                case "1 Year": durationMillis = 365L * 24 * 60 * 60 * 1000; break;
                default: durationMillis = 30L * 24 * 60 * 60 * 1000; break;
            }

            int remainingDays = (int) (durationMillis / (24 * 60 * 60 * 1000));

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            String readableStartDate = sdf.format(new Date(currentTimeMillis));
            String readableEndDate = sdf.format(new Date(currentTimeMillis + durationMillis));

            Booking booking = new Booking(
                    name,
                    mobile,
                    arrivalTime,
                    exitTime,
                    plan,
                    "Seat " + (selectedSeat + 1),
                    actualLabName,
                    readableStartDate,
                    readableEndDate,
                    remainingDays
            );
            booking.setLabId(labId);

            String userId = "user123";
            DatabaseReference userBookingRef = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(userId)
                    .child("bookings")
                    .child(labId + "_seat_" + selectedSeat);

            userBookingRef.setValue(booking).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    seatRef.child("seatStatus").child(String.valueOf(selectedSeat))
                            .child("isBooked").setValue(true)
                            .addOnCompleteListener(task2 -> {
                                if (task2.isSuccessful()) {
                                    Toast.makeText(this, "Seat booked successfully!", Toast.LENGTH_SHORT).show();
                                    loadSeats();
                                    selectedSeat = -1;
                                } else {
                                    Toast.makeText(this, "Failed to update seat status", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show();
                }
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to fetch lab name", Toast.LENGTH_SHORT).show();
        });
    }
}
