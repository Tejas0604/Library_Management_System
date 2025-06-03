package com.example.librarymanagement;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class seatcount extends AppCompatActivity {

    GridLayout gridSeats;
    int roomNo;
    int seatCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_seat);

        gridSeats = findViewById(R.id.gridSeats);

        roomNo = getIntent().getIntExtra("ROOM_NO", 0);
        seatCount = getIntent().getIntExtra("SEAT_COUNT", 0);

        displaySeats(seatCount);
    }

    private void displaySeats(int seatCount) {
        gridSeats.removeAllViews();

        for (int i = 1; i <= seatCount; i++) {
            Button seatBtn = new Button(this);
            seatBtn.setText(String.valueOf(i));

            // Simulated: Even seats are allocated (red), odd are available (green)
            boolean isAllocated = (i % 2 == 0);

            seatBtn.setBackgroundColor(isAllocated ? Color.RED : Color.GREEN);
            seatBtn.setTextColor(Color.WHITE);

            // Set layout with margin
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(8, 8, 8, 8);
            seatBtn.setLayoutParams(params);

            int finalI = i;
            seatBtn.setOnClickListener(view -> {
                String status = isAllocated ? "Allocated" : "Available";
                Toast.makeText(seatcount.this,
                        "Seat " + finalI + " in Room " + roomNo + "\nStatus: " + status,
                        Toast.LENGTH_SHORT).show();
            });

            gridSeats.addView(seatBtn);
        }
    }
}
