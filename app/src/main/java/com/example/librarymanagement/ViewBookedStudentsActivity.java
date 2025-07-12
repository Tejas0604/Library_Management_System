package com.example.librarymanagement;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.librarymanagement.BookedStudentsAdapter;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ViewBookedStudentsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Booking> bookingList;
    BookedStudentsAdapter adapter;
    String labId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booked_students);

        recyclerView = findViewById(R.id.recyclerLibraries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        labId = getIntent().getStringExtra("labId");
        if (labId == null) {
            Toast.makeText(this, "Lab ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        bookingList = new ArrayList<>();
        adapter = new BookedStudentsAdapter(this, bookingList);
        recyclerView.setAdapter(adapter);

        loadBookings();
    }

    private void loadBookings() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    if (userSnap.hasChild("bookings")) {
                        DataSnapshot bookings = userSnap.child("bookings");
                        for (DataSnapshot bookingSnap : bookings.getChildren()) {
                            String key = bookingSnap.getKey();
                            if (key != null && key.startsWith(labId)) {
                                try {
                                    Booking b = bookingSnap.getValue(Booking.class);
                                    if (b != null && b.getStudentName() != null) {
                                        bookingList.add(b);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                if (bookingList.isEmpty()) {
                    Toast.makeText(ViewBookedStudentsActivity.this, "No bookings found", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewBookedStudentsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
