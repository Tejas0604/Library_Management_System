package com.example.librarymanagement;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentFullDetailsActivity extends AppCompatActivity {

    TextView tvName, tvMobile, tvComingTime, tvGoTime, tvMembership, tvRemainingDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_full_details);

        tvName = findViewById(R.id.tvName);
        tvMobile = findViewById(R.id.tvMobile);
        tvComingTime = findViewById(R.id.tvComingTime);
        tvGoTime = findViewById(R.id.tvGoTime);
        tvMembership = findViewById(R.id.tvMembership);
        tvRemainingDays = findViewById(R.id.tvRemainingDays);

        tvName.setText("Name           : " + getIntent().getStringExtra("studentName"));
        tvMobile.setText("Mobile No.     : " + getIntent().getStringExtra("mobile"));
        tvComingTime.setText("Entry Time     : " + getIntent().getStringExtra("comingTime" ));
        tvGoTime.setText("Exit Time      : " + getIntent().getStringExtra("goTime" ));
        tvMembership.setText("Plan           : " + getIntent().getStringExtra("membershipPlan"));
        tvRemainingDays.setText("Remaining Days : " + getIntent().getStringExtra("remainingDays"));

    }
}
