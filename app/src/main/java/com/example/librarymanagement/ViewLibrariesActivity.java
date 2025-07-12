package com.example.librarymanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewLibrariesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    libraryAdapter adapter;
    List<AdminFirstPageHelper> libraryList = new ArrayList<>();
    DatabaseReference labRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_libraries);

        recyclerView = findViewById(R.id.recyclerLibraries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new libraryAdapter(libraryList);
        recyclerView.setAdapter(adapter);

        labRef = FirebaseDatabase.getInstance().getReference("lab_info");

        labRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                libraryList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    AdminFirstPageHelper helper = snap.getValue(AdminFirstPageHelper.class);
                    if (helper != null) {
                        libraryList.add(helper);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewLibrariesActivity.this, "Failed to load libraries", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
