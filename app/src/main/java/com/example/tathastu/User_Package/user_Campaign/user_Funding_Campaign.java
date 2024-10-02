package com.example.tathastu.User_Package.user_Campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.user_Global_Class.ConnectivityReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class user_Funding_Campaign extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{
    private ConnectivityReceiver connectivityReceiver;

    ListView clist;
    List<campaigns> campaignsList;
    DatabaseReference reference;
    String[] cname ={
            "Beti Bachao Beti Padhao",
            "We Raise By Lifting Others",
            "Animal Rescue",
            "Woman Empowerment",
    };
    String[] desc ={
            "Beti Bachao Beti Padhao is a campaign launched by Goverment of india.It mainly ensure financial security for the girl child. the scheme also tries to ensure education of the girl child and her participation in society.",
            "We Raise By Lifting Others was created by Khushi Orphanage. It was established to support orphans in orphanages.This campaign aims to provide shelter , food , cloths and other services for orphans.",
            "Animal Rescue campaign is running by Jivdaya Trust. The Goal is to rescue abandoned and stray animals and get them adopted, to prevent rare and extinst spices of animals and misery of each and every animals and birds.",
            "Woman Empowerment is running by tathastu, this campaign aims is to supports womans by providing education and other services to the woman in specially rural areas.",
    };
    String[] oname = {
            "Indian Goverment",
            "Khushi Orphanage",
            "Jivdaya Trust",
            "Tathastu trust"
    };
    String[] ocontact = {
            "9856231478",
            "9985637562",
            "8595231478",
            "9856698532"
    };

    Integer[] totalfund = {
            350000,400000,360000,200000
    };
    Integer[] donatedfund = {
            150000,200000,260000,85000
    };
    Integer[] imgid = {
            R.drawable.campaign_beti_bachao,
            R.drawable.campaign_orphan,
            R.drawable.campaign_animal,
            R.drawable.campaign_woman,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_funding_campaign);


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
//                Intent i =new Intent(Event_Notifications_Screen.this, DashBoard_Screen.class);
//                startActivity(i);
                finish();
                Animatoo.INSTANCE.animateSlideRight(user_Funding_Campaign.this);
            }
        });

        clist = (ListView) findViewById(R.id.clist);

        ArrayList<campaigns> campaignsList = new ArrayList<>();
        userCampaignListAdapter cadapter = new userCampaignListAdapter(user_Funding_Campaign.this, campaignsList);
        clist.setAdapter(cadapter);
        clist.setClickable(true);

        reference = FirebaseDatabase.getInstance().getReference().child("campaigns");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    campaignsList.clear();

                    // adding data in Array List

                    for(DataSnapshot cdataSnapshot : snapshot.getChildren()){
                        campaigns c = cdataSnapshot.getValue(campaigns.class);

                        campaignsList.add(c);
                        //Log.d("Campaigns", "Name: " + campaigns.getCname());

                    }

                    cadapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseDatabase", "Error reading data", error.toException());
            }

        });

        clist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent i = new Intent(user_Funding_Campaign.this,user_Funding_indetails_Campaign.class);
                i.putExtra("key",campaignsList.get(position).getKey());
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(user_Funding_Campaign.this);
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