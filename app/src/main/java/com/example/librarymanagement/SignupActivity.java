package com.example.librarymanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {

    EditText signupName, signupDepartment, signupUsername, signupPassword, signupPhone;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    RadioGroup userTypeGroup;
    String selectedText = "Student"; // Default to "Student"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Internet Check
        if (!isInternetAvailable()) {
            showInternetPopup();
        }

        // Already logged in? Go to MainActivity
        SharedPreferences sharedPreferences = getSharedPreferences("LibraryAppPrefs", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }

        // Initialize views
        signupName = findViewById(R.id.signup_name);
        signupPhone = findViewById(R.id.signup_phone);
        signupDepartment = findViewById(R.id.signup_dept);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_passw);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        userTypeGroup = findViewById(R.id.userTypeGroup);

        userTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadio = findViewById(checkedId);
            if (selectedRadio != null) {
                selectedText = selectedRadio.getText().toString();
            }
        });

        signupButton.setOnClickListener(v -> {
            String name = signupName.getText().toString().trim();
            String phone = signupPhone.getText().toString().trim();
            String department = signupDepartment.getText().toString().trim();
            String username = signupUsername.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();

            if (validateName(name) && validatePhone(phone) && validateDept(department)
                    && validateUsername(username) && validatePassword(password)) {

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users_registered");

                Query checkUsername = reference.orderByChild("username").equalTo(username);
                checkUsername.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            signupUsername.setError("Username already exists!");
                            signupUsername.requestFocus();
                        } else {
                            HelperClass helperClass = new HelperClass(name, phone, department, username, password, selectedText);
                            reference.child(username).setValue(helperClass);
                            Toast.makeText(SignupActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SignupActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        loginRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    // Validation methods
    private Boolean validateName(String name) {
        if (name.isEmpty()) {
            signupName.setError("Enter name");
            return false;
        }
        signupName.setError(null);
        return true;
    }

    private Boolean validatePhone(String phone) {
        if (phone.isEmpty() || phone.length() < 10 || !phone.matches("\\d+")) {
            signupPhone.setError("Enter valid phone number");
            return false;
        }
        signupPhone.setError(null);
        return true;
    }

    private Boolean validateDept(String dept) {
        if (dept.isEmpty()) {
            signupDepartment.setError("Enter department");
            return false;
        }
        signupDepartment.setError(null);
        return true;
    }

    private Boolean validateUsername(String username) {
        if (username.isEmpty()) {
            signupUsername.setError("Enter username");
            return false;
        }
        signupUsername.setError(null);
        return true;
    }

    private Boolean validatePassword(String password) {
        if (password.isEmpty()) {
            signupPassword.setError("Enter password");
            return false;
        }
        signupPassword.setError(null);
        return true;
    }

    // Internet check
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    private void showInternetPopup() {
        new AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please check your internet connection and try again.")
                .setPositiveButton("OK", (dialog, which) -> {
                    if (!isInternetAvailable()) showInternetPopup();
                })
                .setCancelable(false)
                .show();
    }
}
