package com.example.librarymanagement;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

public class SeatAllocation extends AppCompatActivity {

    GridLayout gridSeats;
    int roomNo, seatCount;

    HashMap<Integer, Boolean> seatStatus = new HashMap<>();
    HashMap<Integer, String> seatDetails = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_allocate);

       // gridSeats = findViewById(R.id.gridSeats);
        roomNo = getIntent().getIntExtra("ROOM_NO", 0);
        seatCount = getIntent().getIntExtra("SEAT_COUNT", 0);

        displaySeats(seatCount);
    }

    private void displaySeats(int seatCount) {
        gridSeats.removeAllViews();

        for (int i = 1; i <= seatCount; i++) {
            Button seatBtn = new Button(this);
            seatBtn.setText(String.valueOf(i));
            seatBtn.setLayoutParams(new GridLayout.LayoutParams());
            seatBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

            int finalI = i;

            seatBtn.setOnClickListener(view -> {
                if (seatStatus.containsKey(finalI) && seatStatus.get(finalI)) {
                    Toast.makeText(SeatAllocation.this,
                            "Seat " + finalI + " is already allocated.\n" + seatDetails.get(finalI),
                            Toast.LENGTH_LONG).show();
                } else {
                    showAllocationDialog(finalI, seatBtn);
                }
            });

            gridSeats.addView(seatBtn);
        }
    }

    private void showAllocationDialog(int seatNumber, Button seatBtn) {
        View dialogView = getLayoutInflater().inflate(R.layout.admin_seat, null);
        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etMobile = dialogView.findViewById(R.id.etMobile);
        EditText etStartDate = dialogView.findViewById(R.id.etStartDate);
        EditText etEndDate = dialogView.findViewById(R.id.etEndDate);

        new AlertDialog.Builder(this)
                .setTitle("Allocate Seat " + seatNumber)
                .setView(dialogView)
                .setPositiveButton("Allocate", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String mobile = etMobile.getText().toString().trim();
                    String startDate = etStartDate.getText().toString().trim();
                    String endDate = etEndDate.getText().toString().trim();

                    if (name.isEmpty() || mobile.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                        Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    seatStatus.put(seatNumber, true);
                    seatDetails.put(seatNumber, "Name: " + name + "\nMobile: " + mobile +
                            "\nFrom: " + startDate + "\nTo: " + endDate);

                    seatBtn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
