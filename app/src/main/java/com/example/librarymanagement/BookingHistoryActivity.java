package com.example.librarymanagement;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class BookingHistoryActivity extends AppCompatActivity {
    RecyclerView historyRecyclerView;
    BookingHistoryAdapter adapter;
    List<Booking> historyList = new ArrayList<>();
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingHistoryAdapter(historyList);
        historyRecyclerView.setAdapter(adapter);

        userId = FirebaseAuth.getInstance().getUid();
        loadHistory();
    }

    private void loadHistory() {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("history")
                .child(userId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Booking booking = snap.getValue(Booking.class);
                    if (booking != null) {
                        historyList.add(booking);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BookingHistoryActivity.this, "Error loading history", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
