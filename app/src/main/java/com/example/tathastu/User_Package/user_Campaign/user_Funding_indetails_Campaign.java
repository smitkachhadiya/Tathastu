package com.example.tathastu.User_Package.user_Campaign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class user_Funding_indetails_Campaign extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ConnectivityReceiver connectivityReceiver;


    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;
    MaterialTextView cname,cdonated,cdescription,oname,ocontact;
    ShapeableImageView campaignimage;
    ExtendedFloatingActionButton btn_donate;
    ExtendedFloatingActionButton btn_donation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_funding_indetails_campaign);

        cname =  findViewById(R.id.cname);
        cdonated =  findViewById(R.id.cdonated);
        cdescription =  findViewById(R.id.cdescription);
        oname =  findViewById(R.id.oname);
        ocontact =  findViewById(R.id.ocontact);
        campaignimage =  findViewById(R.id.campaignimage);
        btn_donate = findViewById(R.id.btn_donate);
        btn_donation = findViewById(R.id.btn_donation);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        Intent intent = this.getIntent();
        String key = intent.getStringExtra("key");

        reference = FirebaseDatabase.getInstance().getReference().child("campaigns").child(key);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                campaigns campaign = snapshot.getValue(campaigns.class);
                if (campaign != null) {
                    String description = campaign.getDescription();
                    String ioname = campaign.getOrganizer_name();
                    String iocontact = campaign.getOrganizer_contact();
                    String icdonated = campaign.getDonation_received();
                    String imageUrl = campaign.getImageUrl();
                    String icname = campaign.getName();

                    cdescription.setText(description);
                    cname.setText(icname);
                    cdonated.setText(icdonated);
                    oname.setText(ioname);
                    ocontact.setText(iocontact);
                    Glide.with(user_Funding_indetails_Campaign.this).load(imageUrl).into(campaignimage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        String ioname = intent.getStringExtra("oname");
//        String iocontact = intent.getStringExtra("ocontact");
//        int icdonated = intent.getIntExtra("donatedfund",15000);
//        int ictotal = intent.getIntExtra("totalfund",10000);
//        int imageid = intent.getIntExtra("cimage",R.drawable.bg);



        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(user_Funding_indetails_Campaign.this,user_Campaign_Donate.class);
                i.putExtra("key",key);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(user_Funding_indetails_Campaign.this);
            }
        });

        btn_donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(user_Funding_indetails_Campaign.this,User_Compaign_View_Donation.class);
                i.putExtra("key",key);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(user_Funding_indetails_Campaign.this);
            }
        });

        FloatingActionButton BTN_back=findViewById(R.id.BTN_event_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i =new Intent(Event_Notifications_Screen.this, DashBoard_Screen.class);
//                startActivity(i);
                finish();
                Animatoo.INSTANCE.animateSlideRight(user_Funding_indetails_Campaign.this);
            }
        });
    }
//------------------------------------------------------------------------------------------------

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }
}
