package com.example.librarymanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class LabInfoFragment extends Fragment {

    RecyclerView recyclerView;
    LabInfoAdapter adapter;
    List<LabInfo> labList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lab_info, container, false);

        recyclerView = view.findViewById(R.id.labRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        labList = new ArrayList<>();

        // ✅ FIX: Pass 4 arguments
        adapter = new LabInfoAdapter(getContext(), labList, "user", "LabInfoFragment");

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
                        Toast.makeText(getContext(), "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LabInfoFragment", "Firebase Error", error.toException());
                    }
                });

        return view;
    }
}
