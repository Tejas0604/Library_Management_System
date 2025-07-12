package com.example.librarymanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class StudentDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StudentNameAdapter adapter;
    List<Booking> studentList;
    String labId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        recyclerView = findViewById(R.id.recyclerStudentNames);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        labId = getIntent().getStringExtra("labId");
        studentList = new ArrayList<>();

        adapter = new StudentNameAdapter(this, studentList, labId);
        recyclerView.setAdapter(adapter);

        if (labId == null || labId.isEmpty()) {
            Toast.makeText(this, "Lab ID missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fetchLabNameAndLoadStudents();
    }

    private void fetchLabNameAndLoadStudents() {
        DatabaseReference labRef = FirebaseDatabase.getInstance().getReference("lab_info").child(labId).child("name");
        labRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String actualLabName = snapshot.getValue(String.class);
                loadStudentNames(actualLabName);
            } else {
                Toast.makeText(this, "Lab name not found for given ID", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error getting lab name", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadStudentNames(String actualLabName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();

                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    DataSnapshot bookingsSnap = userSnap.child("bookings");
                    if (bookingsSnap.exists()) {
                        for (DataSnapshot bookingSnap : bookingsSnap.getChildren()) {
                            Booking booking = bookingSnap.getValue(Booking.class);

                            if (booking != null && booking.getLabName() != null &&
                                    booking.getLabName().equalsIgnoreCase(actualLabName)) {
                                studentList.add(booking);
                            }
                        }
                    }
                }

                if (studentList.isEmpty()) {
                    Toast.makeText(StudentDetailsActivity.this, "No bookings found for this lab", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentDetailsActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
