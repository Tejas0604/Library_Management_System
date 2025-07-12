package com.example.librarymanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    RadioGroup radioPaymentMethod;
    Button btnPay;
    FirebaseAuth auth;
    DatabaseReference paymentsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking); // Make sure your XML file is named correctly

        radioPaymentMethod = findViewById(R.id.radioPaymentMethod);
        btnPay = findViewById(R.id.btnPay);
        auth = FirebaseAuth.getInstance();

        String userId = auth.getUid();
        paymentsRef = FirebaseDatabase.getInstance().getReference("payments").child(userId);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioPaymentMethod.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(BookingActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedRadio = findViewById(selectedId);
                String paymentMethod = selectedRadio.getText().toString();

                if (paymentMethod.equalsIgnoreCase("Cash")) {
                    Toast.makeText(BookingActivity.this, "Cash Payment selected. Please pay at the counter.", Toast.LENGTH_SHORT).show();
                    savePaymentToFirebase("Cash");

                } else if (paymentMethod.equalsIgnoreCase("Online")) {
                    Toast.makeText(BookingActivity.this, "Simulating online payment...", Toast.LENGTH_SHORT).show();
                    simulateOnlinePayment();
                }
            }
        });
    }

    private void simulateOnlinePayment() {
        // Simulate payment delay or success
        Toast.makeText(this, "Online Payment Successful!", Toast.LENGTH_LONG).show();
        savePaymentToFirebase("Online");
    }

    private void savePaymentToFirebase(String method) {
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("method", method);
        paymentData.put("timestamp", System.currentTimeMillis());

        paymentsRef.push().setValue(paymentData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(BookingActivity.this, "Payment saved to Firebase", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(BookingActivity.this, "Failed to save payment", Toast.LENGTH_SHORT).show();
            }
        });
    }
}