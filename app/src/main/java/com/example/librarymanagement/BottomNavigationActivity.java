package com.example.librarymanagement;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        bottomNav = findViewById(R.id.bottomNavigationView);

        // Show Lab fragment first (changed from AllLabsFragment to LabInfoFragment)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AllLabsFragment())
                .commit();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();

            if (itemId == R.id.nav_lab) {
                selectedFragment = new AllLabsFragment();   // ✅ Replaced AllLabsFragment
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new OwnerProfileFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });
    }
}
