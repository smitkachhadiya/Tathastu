package com.example.tathastu.NGO_Package.NGO_Campaign;

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
import com.example.tathastu.User_Package.user_Campaign.campaigns;
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

public class NGO_Campaign_indetails extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ConnectivityReceiver connectivityReceiver;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;

    MaterialTextView cname,cdonated,cdescription,oname,ocontact;
    ShapeableImageView campaignimage;
    ExtendedFloatingActionButton btn_editdetails,btn_view_donation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_campaign_indetails);


        cname =  findViewById(R.id.cname);
        cdonated =  findViewById(R.id.cdonated);
        cdescription =  findViewById(R.id.cdescription);
        oname =  findViewById(R.id.oname);
        ocontact =  findViewById(R.id.ocontact);
        campaignimage = findViewById(R.id.campaignimage);
        btn_editdetails = findViewById(R.id.btn_editdetails);
        btn_view_donation = findViewById(R.id.btn_view_donation);

        // Initialize the ConnectivityReceiver
        connectivityReceiver = new ConnectivityReceiver();
        ConnectivityReceiver.connectivityReceiverListener = this;

        // Register the receiver to listen for connectivity changes
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        FloatingActionButton BTN_back=findViewById(R.id.BTN_event_back);
        //BACK
        BTN_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i =new Intent(Event_Notifications_Screen.this, DashBoard_Screen.class);
//                startActivity(i);

                finish();
                Animatoo.INSTANCE.animateSlideRight(NGO_Campaign_indetails.this);
            }
        });


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
                    String icname = campaign.getName();

                    String imageUrl = campaign.getImageUrl();

                    cdescription.setText(description);
                    cname.setText(icname);
                    cdonated.setText(icdonated);
                    oname.setText(ioname);
                    ocontact.setText(iocontact);
                    Glide.with(NGO_Campaign_indetails.this).load(imageUrl).into(campaignimage);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_editdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NGO_Campaign_indetails.this, NGO_Edit_New_Campaign_Request.class);
                i.putExtra("key",key);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Campaign_indetails.this);

            }
        });

        btn_view_donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(NGO_Campaign_indetails.this, NGO_Campaign_View_Donations.class);
                i.putExtra("key",key);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Campaign_indetails.this);
            }
        });
    }

    //-------------------------------------------------------------------------------------------------------------------


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(NGO_Campaign_indetails.this);
        finish();
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