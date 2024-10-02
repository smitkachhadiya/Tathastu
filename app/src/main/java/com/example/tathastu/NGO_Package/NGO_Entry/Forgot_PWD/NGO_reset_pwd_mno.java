package com.example.tathastu.NGO_Package.NGO_Entry.Forgot_PWD;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NGO_reset_pwd_mno extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    TextInputEditText forgot_mno;
    ExtendedFloatingActionButton forgot_send_OTP;

    private ConnectivityReceiver connectivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_reset_pwd_mno);

        forgot_mno =findViewById(R.id.ngo_forgot_mno);
        forgot_send_OTP =findViewById(R.id.ngo_forgot_send_OTP);


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
                finish();
                Animatoo.INSTANCE.animateSlideRight(NGO_reset_pwd_mno.this);
            }
        });

        forgot_send_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mob = forgot_mno.getText().toString();

                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    if (mob.isEmpty()) {
                        // Set an error message
                        showSnackbar(findViewById(android.R.id.content), "Please enter mobile number...");
                    } else if (mob.length() < 10) {
                        showSnackbar(findViewById(android.R.id.content), "Please enter a valid mobile number...");
                    }else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo");

                        int flag[] = {0};

                        NGO_Profile_Model[] data = new NGO_Profile_Model[1];

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {

                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                        data[0] = snapshot1.getValue(NGO_Profile_Model.class);
                                        String mno = data[0].getMobile();

                                        if (("+91" + mob).equals(mno)) {
                                            Toast.makeText(NGO_reset_pwd_mno.this, "Mobile number registered yet", Toast.LENGTH_SHORT).show();
                                            flag[0] = 1;
                                            break;
                                        } else {
                                            flag[0] = 0;
                                        }

                                    }
                                    if (flag[0] == 1) {
                                        String email = data[0].getEmail();
                                        String fname = data[0].getFname();
                                        String password  = data[0].getPassword();

                                        Intent i = new Intent(NGO_reset_pwd_mno.this, NGO_reset_pwd_OTP.class);
                                        i.putExtra("source", "login");
                                        i.putExtra("forgot_number", "+91" + mob);
                                        i.putExtra("email", email);
                                        i.putExtra("fname", fname);
                                        i.putExtra("pwd", password);
                                        i.putExtra("check", "login");

                                        startActivity(i);
                                        Animatoo.INSTANCE.animateSlideLeft(NGO_reset_pwd_mno.this);
                                    } else {
                                        Toast.makeText(NGO_reset_pwd_mno.this, "you haven't registered yet", Toast.LENGTH_SHORT).show();

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }
        });

    }

    //----------------------------------------------------------------------------------------------

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