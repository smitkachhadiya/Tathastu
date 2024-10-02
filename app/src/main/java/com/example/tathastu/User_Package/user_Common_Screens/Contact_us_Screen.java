package com.example.tathastu.User_Package.user_Common_Screens;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;

public class Contact_us_Screen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private ConnectivityReceiver connectivityReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us_screen);

        FloatingActionButton BTN_contact_insta = findViewById(R.id.BTN_contact_insta);
        FloatingActionButton BTN_contact_fb = findViewById(R.id.BTN_contact_fb);
        FloatingActionButton BTN_contact_twitter = findViewById(R.id.BTN_contact_twitter);

        MaterialTextView txt_contact_mno = findViewById(R.id.txt_contact_mno);
        MaterialTextView txt_contact_email = findViewById(R.id.txt_contact_email);
        MaterialTextView txt_contact_address = findViewById(R.id.txt_contact_address);
        CardView BTN_contact_feedback=findViewById(R.id.BTN_contact_feedback);

        setCopyToClipboardListener(txt_contact_mno, "+910123456789");
        setCopyToClipboardListener(txt_contact_email, "tathastu052threesofficial@gmail.com");
        setCopyToClipboardListener(txt_contact_address, "123 ABC Street, City Light Area, Surat 395007, Gujarat, India.");



        String source = getIntent().getStringExtra("source");
        if("admin_contact".equals(source)){
            BTN_contact_feedback.setVisibility(View.GONE);
        }


        FloatingActionButton BTN_back=findViewById(R.id.BTN_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i =new Intent(Contact_us_Screen.this, DashBoard_Screen.class);
//                startActivity(i);
                finish();
                Animatoo.INSTANCE.animateSlideRight(Contact_us_Screen.this);
            }
        });

        //FEEDBACK
        BTN_contact_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnected()) {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                    return;
                } else {
                    Intent i =new Intent(Contact_us_Screen.this, FeedBack_Screen.class);
                    i.putExtra("source","contact");
                    startActivity(i);
                    Animatoo.INSTANCE.animateSlideLeft(Contact_us_Screen.this);
                }

            }
        });

// Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //FOR FOLLOW US ON
        BTN_contact_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    String instagramLink = "https://www.instagram.com/tathastu.g052?igsh=OGQ5ZDc2ODk2ZA==\n";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramLink));
                    startActivity(i);
                    Animatoo.INSTANCE.animateSwipeLeft(Contact_us_Screen.this);
                } else {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                }
            }
        });

        BTN_contact_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    String facebookLink = "https://www.facebook.com/tathastu.g052?mibextid=ZbWKwL\n";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink));
                    startActivity(i);
                    Animatoo.INSTANCE.animateSwipeLeft(Contact_us_Screen.this);
                } else {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                }
            }
        });

        BTN_contact_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                    String twitterLink = "https://twitter.com/tathastu_g052?t=MrAtvaTuvcF1hbSMDqd5_A&s=09\n";
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink));
                    startActivity(i);
                    Animatoo.INSTANCE.animateSwipeLeft(Contact_us_Screen.this);
                }

                else {
                    showSnackbar(findViewById(android.R.id.content), "Please check your internet connection...");
                }
            }
        });

    }
//----------------------------------------------------------------------------------
@Override
public void onBackPressed() {
    super.onBackPressed();
    finish();
    Animatoo.INSTANCE.animateSlideRight(Contact_us_Screen.this);
}
    private void setCopyToClipboardListener(MaterialTextView textView, final String text) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Copy the text to the clipboard
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Contact Info", text);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(Contact_us_Screen.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(connectivityReceiver);
    }
    // Helper method to check if the device is connected to the internet
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
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