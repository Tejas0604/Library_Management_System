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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText loginUsername, loginPassword;
    Button loginButton;
    TextView signupRedirectText;
    RadioGroup userTypeGroup;
    String selectedText = "Student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        if (!isInternetAvailable()) {
            showInternetPopup();
        }

        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_passw);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        userTypeGroup = findViewById(R.id.userTypeGroup);

        loginButton.setOnClickListener(v -> {
            if (validateUsername() && validatePassword()) {
                checkUser();
            }
        });

        signupRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        });

        userTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedRadio = findViewById(checkedId);
            if (selectedRadio != null) {
                selectedText = selectedRadio.getText().toString();
            }
        });
    }

    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Enter Username");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Enter Password");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users_registered");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        Object passwordObj = userSnapshot.child("password").getValue();
                        Object userTypeObj = userSnapshot.child("userType").getValue();

                        String passwordFromDB = String.valueOf(passwordObj);
                        String userTypeFromDB = String.valueOf(userTypeObj);

                        if (passwordFromDB != null && passwordFromDB.equals(userPassword)) {
                            if (userTypeFromDB != null && userTypeFromDB.equalsIgnoreCase(selectedText)) {
                                if (selectedText.equalsIgnoreCase("Owner")) {
                                    checkOwnerLabAndRedirect(userUsername);
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                return;
                            } else {
                                loginPassword.setError("Incorrect User Type");
                                loginPassword.requestFocus();
                                return;
                            }
                        } else {
                            loginPassword.setError("Incorrect Password");
                            loginPassword.setText("");
                            loginPassword.requestFocus();
                            return;
                        }
                    }
                } else {
                    loginUsername.setError("Username does not exist");
                    loginUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void checkOwnerLabAndRedirect(String username) {
        SharedPreferences prefs = getSharedPreferences("LibraryAppPrefs", MODE_PRIVATE);
        prefs.edit().putString("username", username).apply();

        DatabaseReference labRef = FirebaseDatabase.getInstance().getReference("lab_info");
        Query query = labRef.orderByChild("ownerUsername").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent;
                if (snapshot.exists()) {
                    intent = new Intent(LoginActivity.this, BottomNavigationActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, AdminFirstPage.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Error checking lab: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
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
