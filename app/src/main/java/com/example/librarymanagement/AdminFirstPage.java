package com.example.librarymanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class AdminFirstPage extends AppCompatActivity {

    EditText et_location, et_location2, et_building, et_floor, et_room, et_seats, et_start_time, et_end_time;
    Button btn_add_seat;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_first_page); // If your XML is named this way

        et_location = findViewById(R.id.et_location);
        et_location2 = findViewById(R.id.et_location2);
        et_building = findViewById(R.id.et_building);
        et_floor = findViewById(R.id.et_floor);
        et_room = findViewById(R.id.et_room);
        et_seats = findViewById(R.id.et_seats);
        et_start_time = findViewById(R.id.et_start_time);
        et_end_time = findViewById(R.id.et_end_time);
        btn_add_seat = findViewById(R.id.btn_add_seat);

        btn_add_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (
                        et_location.getText().toString().trim().isEmpty() ||
                                et_location2.getText().toString().trim().isEmpty() ||
                                et_building.getText().toString().trim().isEmpty() ||
                                et_floor.getText().toString().trim().isEmpty() ||
                                et_room.getText().toString().trim().isEmpty() ||
                                et_seats.getText().toString().trim().isEmpty() ||
                                et_start_time.getText().toString().trim().isEmpty() ||
                                et_end_time.getText().toString().trim().isEmpty()
                ) {
                    Toast.makeText(AdminFirstPage.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                String name = et_location.getText().toString();
                String end_time = et_end_time.getText().toString();
                String start_time = et_start_time.getText().toString();

                int fee = Integer.parseInt(et_location2.getText().toString());
                String location = et_building.getText().toString();
                int floor = Integer.parseInt(et_floor.getText().toString());
                int room = Integer.parseInt(et_room.getText().toString());
                int seats = Integer.parseInt(et_seats.getText().toString());

                if (fee <= 0 || floor <= 0 || room <= 0 || seats < 10) {
                    Toast.makeText(AdminFirstPage.this, "Fee, floor, room must be > 0 and seats must be ≥ 10", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int start = Integer.parseInt(start_time.replaceAll("[^0-9]", ""));
                    int end = Integer.parseInt(end_time.replaceAll("[^0-9]", ""));
                    if (start < 1 || start > 12 || end < 1 || end > 12) {
                        Toast.makeText(AdminFirstPage.this, "Start and End time must be between 1 and 12", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    Toast.makeText(AdminFirstPage.this, "Invalid time format", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("LibraryAppPrefs", MODE_PRIVATE);
                String ownerUsername = prefs.getString("username", "unknown");

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("lab_info");

                DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference("lab_info");
                Query query = checkRef.orderByChild("ownerUsername").equalTo(ownerUsername);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            AdminFirstPageHelper existing = child.getValue(AdminFirstPageHelper.class);
                            if (existing != null && existing.getName().equalsIgnoreCase(name)) {
                                Toast.makeText(AdminFirstPage.this, "Library name already exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        String labId = reference.push().getKey();
                        AdminFirstPageHelper helper = new AdminFirstPageHelper(
                                labId, name, location, start_time, end_time, floor, room, seats, fee, ownerUsername
                        );

                        reference.child(labId).setValue(helper);

                        DatabaseReference seatStatusRef = reference.child(labId).child("seatStatus");
                        for (int i = 0; i < seats; i++) {
                            Seat seat = new Seat(i, false);
                            seatStatusRef.child(String.valueOf(i)).setValue(seat);
                        }

                        Toast.makeText(AdminFirstPage.this, "Lab and seats added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminFirstPage.this, BottomNavigationActivity.class)); // ✅ Used existing working activity
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminFirstPage.this, "Error checking library name", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
