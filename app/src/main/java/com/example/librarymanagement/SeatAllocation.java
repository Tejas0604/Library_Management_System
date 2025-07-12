package com.example.librarymanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class SeatAllocation extends AppCompatActivity {

    GridLayout seatGrid;
    EditText etName, etMobile, etComingTime, etGoTime;
    Spinner spinnerPlan;
    Button btnBook;
    String labId, seatId = "";
    DatabaseReference seatRef;
    int seatCount;
    List<Button> seatButtons = new ArrayList<>();
    String selectedPlan = "";
    String actualLabName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_allocation);

        seatGrid = findViewById(R.id.seatGrid);
        etName = findViewById(R.id.etStudentName);
        etMobile = findViewById(R.id.etMobile);
        etComingTime = findViewById(R.id.etComingTime);
        etGoTime = findViewById(R.id.etGoTime);
        spinnerPlan = findViewById(R.id.spinnerPlan);
        btnBook = findViewById(R.id.btnBook);

        labId = getIntent().getStringExtra("labId");
        seatCount = getIntent().getIntExtra("seatCount", 0);
        seatRef = FirebaseDatabase.getInstance().getReference("labs").child(labId).child("seats");

        actualLabName = labId; // default name if not fetched from DB

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.membership_plans, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlan.setAdapter(adapter);

        spinnerPlan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedPlan = parent.getItemAtPosition(pos).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        loadSeatStatus();

        btnBook.setOnClickListener(v -> {
            if (seatId.isEmpty()) {
                Toast.makeText(this, "Select a seat", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = etName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String arrival = etComingTime.getText().toString().trim();
            String exit = etGoTime.getText().toString().trim();

            if (name.isEmpty() || mobile.isEmpty() || arrival.isEmpty() || exit.isEmpty() || selectedPlan.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mobile.length() != 10) {
                Toast.makeText(this, "Enter valid 10-digit mobile number", Toast.LENGTH_SHORT).show();
                return;
            }

            int remainingDays = getPlanDays(selectedPlan);

            Calendar cal = Calendar.getInstance();
            String readableStartDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, remainingDays);
            String readableEndDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(cal.getTime());

            Booking booking = new Booking();
            booking.setLabName(actualLabName);
            booking.setSeatNumber(seatId);
            booking.setMembershipPlan(selectedPlan);
            booking.setRemainingDays(remainingDays);
            booking.setStudentName(name);
            booking.setMobileNumber(mobile);
            booking.setComingTime(arrival);
            booking.setGoTime(exit);
            booking.setStartDate(readableStartDate);
            booking.setEndDate(readableEndDate);

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String bookingKey = labId + "_" + seatId;
            String pushKey = FirebaseDatabase.getInstance().getReference().push().getKey();

            DatabaseReference userBookingRef = FirebaseDatabase.getInstance()
                    .getReference("users").child(userId).child("bookings").child(bookingKey);


            DatabaseReference labBookingRef = FirebaseDatabase.getInstance()
                    .getReference("lab_info").child(labId).child("bookings").child(pushKey);

            userBookingRef.setValue(booking);
            labBookingRef.setValue(booking);

            seatRef.child(seatId).setValue(true); // true = booked

            Toast.makeText(this, "Seat booked", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void loadSeatStatus() {
        seatGrid.removeAllViews();
        seatButtons.clear();

        for (int i = 1; i <= seatCount; i++) {
            String seatKey = "Seat" + i;
            Button seatButton = new Button(this);
            seatButton.setText(seatKey);
            seatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            seatButton.setPadding(10, 10, 10, 10);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(8, 8, 8, 8);
            seatButton.setLayoutParams(params);

            String finalSeatKey = seatKey;
            seatButton.setOnClickListener(v -> {
                for (Button b : seatButtons) {
                    b.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                }
                seatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
                seatId = finalSeatKey;
            });

            seatButtons.add(seatButton);
            seatGrid.addView(seatButton);
        }

        seatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    Boolean isBooked = ds.getValue(Boolean.class);
                    if (Boolean.TRUE.equals(isBooked)) {
                        for (Button btn : seatButtons) {
                            if (btn.getText().toString().equals(key)) {
                                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                                btn.setEnabled(false);
                            }
                        }
                    }
                }
            }

            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private int getPlanDays(String plan) {
        if (plan.contains("1 Month")) return 30;
        if (plan.contains("3 Months")) return 90;
        if (plan.contains("6 Months")) return 180;
        if (plan.contains("1 Year")) return 365;
        return 30;
    }
}
