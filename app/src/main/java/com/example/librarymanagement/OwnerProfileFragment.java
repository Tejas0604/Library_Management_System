package com.example.librarymanagement;

import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.google.firebase.database.*;

import java.util.*;

public class OwnerProfileFragment extends Fragment {

    RecyclerView recyclerView;
    LabInfoAdapter adapter;
    List<LabInfo> labList;
    Button btnAddNewLab;

    public OwnerProfileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_owner_profile, container, false);

        recyclerView = view.findViewById(R.id.ownerLabRecyclerView);
        btnAddNewLab = view.findViewById(R.id.btn_add_new_lab);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        labList = new ArrayList<>();

        // ✅ Corrected constructor usage
        adapter = new LabInfoAdapter(getContext(), labList, "owner", "OwnerProfile");
        recyclerView.setAdapter(adapter);

        btnAddNewLab.setOnClickListener(v ->
                startActivity(new Intent(getContext(), AdminFirstPage.class)));

        loadOwnerLabs();
        return view;
    }

    private void loadOwnerLabs() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("LibraryAppPrefs", Context.MODE_PRIVATE);
        String ownerUsername = prefs.getString("username", null);

        if (ownerUsername == null) {
            Toast.makeText(getContext(), "Owner not logged in properly", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("lab_info");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                labList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    LabInfo lab = ds.getValue(LabInfo.class);
                    if (lab != null && ownerUsername.equals(lab.ownerUsername)) {
                        lab.labId = ds.getKey();
                        labList.add(lab);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("OwnerProfileFragment", "Error: ", error.toException());
                Toast.makeText(getContext(), "Error loading labs", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
