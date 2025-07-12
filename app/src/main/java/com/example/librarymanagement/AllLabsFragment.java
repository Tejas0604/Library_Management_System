package com.example.librarymanagement;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.google.firebase.database.*;

import java.util.*;

public class AllLabsFragment extends Fragment {

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

        // ✅ Pass userRole and source explicitly
        adapter = new LabInfoAdapter(getContext(), labList, "user", "AllLabsFragment");
        recyclerView.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference("lab_info")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        labList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            LabInfo lab = ds.getValue(LabInfo.class);
                            if (lab != null) {
                                lab.labId = ds.getKey();
                                labList.add(lab);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AllLabsFragment", "Firebase Error", error.toException());
                    }
                });

        return view;
    }
}
