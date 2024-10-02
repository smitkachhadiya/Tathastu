package com.example.tathastu.Common_Screens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.tathastu.Admin_Package.Admin_Entry.Admin_Login_Screen;
import com.example.tathastu.NGO_Package.NGO_Entry.NGO_Login_Screen;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Entry.Login_Screen;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.snackbar.Snackbar;

public class Selection_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    CardView card_select_user, card_select_admin, card_select_ngo;

    private ConnectivityReceiver connectivityReceiver;

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 100;
    private static final int NETWORK_PERMISSION_REQUEST_CODE = 200;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 300;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 400;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_screen);

        card_select_user = findViewById(R.id.card_select_user);
        card_select_admin = findViewById(R.id.card_select_admin);
        card_select_ngo = findViewById(R.id.card_select_ngo);


        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        card_select_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    YoYo.with(Techniques.Tada)
                            .duration(1000)
                            .repeat(0)
                            .playOn(card_select_user);
                    Intent i = new Intent(Selection_Screen.this, Login_Screen.class);
                    startActivity(i);
                    Animatoo.INSTANCE.animateZoom(Selection_Screen.this);
                }
            }
        });
        //CHANGES IN CLASS FILE
        card_select_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    YoYo.with(Techniques.Tada)
                            .duration(1000)
                            .repeat(0)
                            .playOn(card_select_admin);
                    Intent i = new Intent(Selection_Screen.this, Admin_Login_Screen.class);
                    startActivity(i);
                    Animatoo.INSTANCE.animateZoom(Selection_Screen.this);
                }
            }
        });
        //CHANGES IN CLASS FILE
        card_select_ngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInternetAvailable()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    YoYo.with(Techniques.Tada)
                            .duration(1000)
                            .repeat(0)
                            .playOn(card_select_ngo);
                    Intent i = new Intent(Selection_Screen.this, NGO_Login_Screen.class);
                    startActivity(i);
                    Animatoo.INSTANCE.animateZoom(Selection_Screen.this);
                }
            }
        });

        checkStoragePermission();

    }


    //----------------------------------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        startActivity(new Intent(Selection_Screen.this,After_Intro_Start.class));
        Animatoo.INSTANCE.animateSlideRight(Selection_Screen.this);
        finish();
        super.onBackPressed();
    }

    //FILE ACCESS PERMISSION
    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(Selection_Screen.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            // Request permission
            ActivityCompat.requestPermissions(Selection_Screen.this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);
        }

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
