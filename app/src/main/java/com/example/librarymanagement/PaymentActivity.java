package com.example.librarymanagement;

import android.os.Bundle;
import android.widget.*;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {
    RadioGroup radioPaymentMethod;
    RadioButton radioCash, radioOnline;
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        radioPaymentMethod = findViewById(R.id.radioPaymentMethod);
        radioCash = findViewById(R.id.radioCash);
        radioOnline = findViewById(R.id.radioOnline);
        btnPay = findViewById(R.id.btnPay);

        btnPay.setOnClickListener(v -> {
            String method = radioCash.isChecked() ? "Cash" : "Online";
            Toast.makeText(this, "Payment method: " + method, Toast.LENGTH_SHORT).show();
        });
    }
}
