package com.example.tathastu.NGO_Package.NGO_Event;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Event.events;
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

public class NGO_Event_inDetails extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ConnectivityReceiver connectivityReceiver;

    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressBar eprogress;
    MaterialTextView ename, oname, edesc, edate, eaddress, ecity, eparticipated, etotal;
    ShapeableImageView eventimage;
    FloatingActionButton btn_editevent;

    ExtendedFloatingActionButton btn_volunteer;
    String iename,iedes,ieparticipated,ietotal,ieaddress,iecity,iedate,ioname,imageUrl,key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_event_in_details);

        eprogress =  findViewById(R.id.eprogress);
        ename =  findViewById(R.id.ename);
        edesc =  findViewById(R.id.edesc);
        edate =  findViewById(R.id.edate);
        oname =  findViewById(R.id.oname);
        eaddress =  findViewById(R.id.eaddress);
        ecity =  findViewById(R.id.ecity);
        eparticipated =  findViewById(R.id.eparticipated);
        etotal =  findViewById(R.id.etotal);
        eventimage = findViewById(R.id.eventimage);
        btn_volunteer = findViewById(R.id.btn_volunteer);
        btn_editevent = findViewById(R.id.btn_editevent);

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

                finish();Animatoo.INSTANCE.animateSlideRight(NGO_Event_inDetails.this);
            }
        });

        Intent intent = this.getIntent();
        key = intent.getStringExtra("key");

        reference = FirebaseDatabase.getInstance().getReference().child("events").child(key);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                events events = snapshot.getValue(events.class);
                if (events != null) {
                    iedes = events.getDescription();
                    iename = events.getName();
                    ioname = events.getOrganizer_name();
                    iedate = events.getDate();
                    ieaddress = events.getAddress();
                    iecity = events.getCity();
                    ietotal = events.getTotal_volunteer();
                    ieparticipated = events.getVolunteer_get();
                    imageUrl = events.getImageUrl();

                    eprogress.setMax(Integer.parseInt(ietotal));
                    eprogress.setProgress(Integer.parseInt(ieparticipated));
                    ename.setText(iename);
                    edesc.setText(iedes);
                    oname.setText(ioname);
                    edate.setText(iedate);
                    eaddress.setText(ieaddress);
                    ecity.setText(iecity);
                    etotal.setText(ietotal);
                    eparticipated.setText(ieparticipated);
                    Glide.with(NGO_Event_inDetails.this).load(imageUrl).into(eventimage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_editevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NGO_Event_inDetails.this,NGO_Event_Edit_Request.class);
                i.putExtra("key",key);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Event_inDetails.this);
            }
        });

        btn_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NGO_Event_inDetails.this, NGO_Event_View_Volunteers.class);
                i.putExtra("key",key);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(NGO_Event_inDetails.this);
            }
        });

    }

    //-------------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(NGO_Event_inDetails.this);
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