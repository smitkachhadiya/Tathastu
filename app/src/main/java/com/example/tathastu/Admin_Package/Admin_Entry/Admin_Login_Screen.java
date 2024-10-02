package com.example.tathastu.Admin_Package.Admin_Entry;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.Admin_Package.Admin_DashBoard.Admin_DashBoard_Screen;
import com.example.tathastu.Common_Screens.Selection_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_Login_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    public ExtendedFloatingActionButton BTN_login_admin;
    private TextInputEditText edtemail_admin, edtpwd_admin;
    private FirebaseAuth mAuth;
    private ConnectivityReceiver connectivityReceiver;
    public static final String PREFS_NAME_ADMIN = "myprefs_admin";
    public static final String KEY_FIRST_TIME_LOGIN_ADMIN = "FirstTimeLogin_admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_screen);

        mAuth = FirebaseAuth.getInstance();

        BTN_login_admin = findViewById(R.id.BTN_login_admin);
        edtemail_admin = findViewById(R.id.txt_Loginemail_admin);
        edtpwd_admin = findViewById(R.id.txt_login_pwd_admin);

        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;
        registerReceiver(connectivityReceiver, getIntentFilter());

        BTN_login_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtemail_admin.getText().toString();
                String pwd = edtpwd_admin.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                    showSnackbar(findViewById(android.R.id.content), "Please enter both email and password");
                    return;
                }

                // Check if the entered email is valid
                if (!isValidEmail(email)) {
                    edtemail_admin.setError("Please enter a valid email address");
                    return;
                }

                // Clear any previous error
                edtemail_admin.setError(null);

                // Check admin credentials in Firebase Realtime Database
                checkAdminCredentials(email, pwd);
            }
        });

    }
    //------------------------------------------------------------------------------------
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-z0-9._%+-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$";
        return email.matches(emailPattern);
    }

    private void checkAdminCredentials(String email, String password) {

        SharedPreferences sharedPreferences = getSharedPreferences("AdminLogin",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("hasLoggedIn",true).apply();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("admin");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                boolean isAdmin = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AdminModel admin = snapshot.getValue(AdminModel.class);

                    if (admin != null && admin.getEmail() != null && admin.getPwd() != null) {
                        // Convert stored email and input email to lowercase and trim
                        String storedEmail = admin.getEmail().toLowerCase().trim();
                        String storedPassword = admin.getPwd().trim();

                        // Convert input email to lowercase and trim
                        String inputEmail = email.toLowerCase().trim();
                        String inputPassword = password.trim();

                        if (storedEmail.equals(inputEmail) && storedPassword.equals(inputPassword)) {

                            SharedPreferences sharedPreferences1 = getSharedPreferences("AdminLogin",MODE_PRIVATE);
                            sharedPreferences1.edit().putString("adminId",admin.getAdminId()).apply();

                            Intent intent = new Intent(Admin_Login_Screen.this,Admin_DashBoard_Screen.class);
                            startActivity(intent);
                            Animatoo.INSTANCE.animateSlideLeft(Admin_Login_Screen.this);
                            break;
                        }
                        else {
                            Toast.makeText(Admin_Login_Screen.this, "Invalid email or passwprd", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

//                // Inside checkAdminCredentials method
//                if (isAdmin) {
//                    // Admin login successful
//                    if (!isFirstTimeLogin()) {
//                        // It's not the first time, navigate to the dashboard
//                        Intent i = new Intent(Admin_Login_Screen.this, Admin_DashBoard_Screen.class);
//                        // Pass admin data to the profile screen if needed
//                        i.putExtra("admin_email", admin.getEmail());
//                        i.putExtra("admin_name", admin.getName());
//                        i.putExtra("admin_mno", admin.getMno());
//                        i.putExtra("admin_pwd",admin.getPwd());
//                        startActivity(i);
//                        Animatoo.INSTANCE.animateSlideLeft(Admin_Login_Screen.this);
//                        finish(); // Optionally finish the login activity
//                    } else {
//                        // It's the first time, navigate to the profile screen
//                        Intent i = new Intent(Admin_Login_Screen.this, Admin_DashBoard_Screen.class);
//                        // Pass admin data to the profile screen if needed
//                        i.putExtra("admin_email", admin.getEmail());
//                        i.putExtra("admin_name", admin.getName());
//                        i.putExtra("admin_mno", admin.getMno());
//                        i.putExtra("admin_pwd",admin.getPwd());
//                        startActivity(i);
//                        Animatoo.INSTANCE.animateSlideLeft(Admin_Login_Screen.this);
//                        finish(); // Optionally finish the login activity
//                    }
//                } else {
//                    // Invalid admin credentials
//                    showSnackbar(findViewById(android.R.id.content), "Invalid email or password.");
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle the error if needed
                Log.e("Admin_Login_Screen", "Database Error: " + databaseError.getMessage());
            }
        });
    }


//    private boolean isFirstTimeLogin() {
//        // Use SharedPreferences to check if it's the first time login
//        SharedPreferences preferences = getSharedPreferences(PREFS_NAME_ADMIN, MODE_PRIVATE);
//        boolean isFirstTime = preferences.getBoolean(KEY_FIRST_TIME_LOGIN_ADMIN, true);
//
//        if (isFirstTime) {
//            // If it's the first time, update the flag in SharedPreferences
//            preferences.edit().putBoolean(KEY_FIRST_TIME_LOGIN_ADMIN, false).apply();
//        }
//
//        return isFirstTime;
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Admin_Login_Screen.this, Selection_Screen.class);
        startActivity(i);
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityReceiver);
    }

    private void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();

        LayoutInflater inflater = LayoutInflater.from(this);
        View customView = inflater.inflate(R.layout.custom_snackbar_layout, null);

        TextView textView = customView.findViewById(android.R.id.text1);
        textView.setText(message);

        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarView;
        snackbarLayout.removeAllViews();
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

    private IntentFilter getIntentFilter() {
        return new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    }
}
