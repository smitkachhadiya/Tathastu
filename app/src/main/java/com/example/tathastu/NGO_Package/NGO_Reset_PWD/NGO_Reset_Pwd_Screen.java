package com.example.tathastu.NGO_Package.NGO_Reset_PWD;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Profile.NGO_Profile_Model;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class NGO_Reset_Pwd_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    TextInputEditText reset_old_pwd,reset_new_pwd,reset_C_new_pwd;
    ExtendedFloatingActionButton reset_change_pwd;
    private ConnectivityReceiver connectivityReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_reset_pwd_screen);

        fetchProfileData();

        reset_old_pwd =findViewById(R.id.reset_old_pwd);
        reset_new_pwd =findViewById(R.id.reset_new_pwd);
        reset_C_new_pwd =findViewById(R.id.reset_C_new_pwd);
        reset_change_pwd =findViewById(R.id.reset_change_pwd);


        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back=findViewById(R.id.BTN_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button based on the source

                finish();Animatoo.INSTANCE.animateSlideRight(NGO_Reset_Pwd_Screen.this);
            }
        });

        reset_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the values from the input fields
                String oldPwd = reset_old_pwd.getText().toString().trim();
                String newPwd = reset_new_pwd.getText().toString().trim();
                String confirmNewPwd = reset_C_new_pwd.getText().toString().trim();

                // Check if any of the fields are empty
                if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmNewPwd.isEmpty()) {
                    showSnackbar(v, "Please fill in all the fields.");
                } else if (newPwd.length() < 8 || confirmNewPwd.length() < 8 || newPwd.length() > 12 || confirmNewPwd.length() > 12) {
                    // Check if new password and confirm new password meet the password length criteria
                    showSnackbar(v, "Password must be between 8 and 12 characters long.");
                } else if (!isValidPassword(newPwd) || !isValidPassword(confirmNewPwd)) {
                    // Check if new password and confirm new password meet the password criteria
                    showSnackbar(v, "Password must contain at least one uppercase letter, one lowercase letter, one special character, and one digit.");
                } else if (!newPwd.equals(confirmNewPwd)) { // Check if the new password and confirm new password match
                    showSnackbar(v, "New password and confirm password do not match.");
                } else {
                    // Perform the password reset process here
                    // Add your logic to reset the password
                    SharedPreferences sharedPreferences1 = getSharedPreferences("NGO", MODE_PRIVATE);
                    String ngoId = sharedPreferences1.getString("ngoId", "");

                    Map<String, Object> map = new HashMap<>();
                    map.put("password", confirmNewPwd);

                    FirebaseDatabase.getInstance().getReference().child("ngo").child(ngoId)
                            .updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(NGO_Reset_Pwd_Screen.this, "Profile Updated Successfully !!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(NGO_Reset_Pwd_Screen.this, "Failed To Update Profile !!", Toast.LENGTH_SHORT).show();
                                    }

                                    //startActivity(new Intent(NGO_Update_Profile.this, NGO_Profile_Screen.class));
                                    finish();
                                    Animatoo.INSTANCE.animateSlideRight(NGO_Reset_Pwd_Screen.this);
                                }
                            });
                    // Show success message if the password is successfully reset
                    showSnackbar(v, "Password reset successful.");
                }


            }
        });

    }

    private void fetchProfileData() {
        SharedPreferences sharedPreferences1 = getSharedPreferences("NGO", MODE_PRIVATE);
        String ngoId = sharedPreferences1.getString("ngoId", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo").child(ngoId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    NGO_Profile_Model data = snapshot.getValue(NGO_Profile_Model.class);
                    String pwd = data.getPassword();

                    reset_old_pwd.setText(pwd);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //----------------------------------------------------------------------------------------------

    // Method to validate the password
    private boolean isValidPassword(String password) {
        // Password should be 8 to 12 characters long and contain at least one uppercase letter, one lowercase letter, one special character, and one digit
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-=_+{};:'\"<>,.?/\\\\|]).{8,12}$";
        return password.matches(passwordPattern);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }

    //HIDE THE KEYBOARD
    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // Helper method to check if the internet connection is available
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(connectivityReceiver);
    }
    //SNACKBAR
    private void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        // Inflate custom layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.custom_snackbar_layout, null);

        // Set text
        TextView textView = customView.findViewById(android.R.id.text1);
        textView.setText(message);

        // Add custom view to Snackbar
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;
        snackbarLayout.removeAllViews(); // Remove all default views
        snackbarLayout.setPadding(1, 1, 1, 1);
        snackbarLayout.addView(customView, 0);

        snackbar.show();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (!isConnected) {
            showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
        }
    }
}