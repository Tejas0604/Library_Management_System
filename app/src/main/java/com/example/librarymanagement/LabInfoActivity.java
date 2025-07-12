package com.example.librarymanagement;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LabInfoActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LabInfoAdapter adapter;
    List<LabInfo> labList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_info);

        recyclerView = findViewById(R.id.labRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        labList = new ArrayList<>();

        // ✅ Pass required parameters to LabInfoAdapter
        adapter = new LabInfoAdapter(this, labList);
        recyclerView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("lab_info")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        labList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            LabInfo lab = ds.getValue(LabInfo.class);
                            if (lab != null) {
                                lab.labId = ds.getKey(); // ✅ Important
                                labList.add(lab);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LabInfoActivity.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LabInfoActivity", "Firebase Error", error.toException());
                    }
                });
    }
}
