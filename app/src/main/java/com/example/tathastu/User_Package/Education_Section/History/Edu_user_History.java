package com.example.tathastu.User_Package.Education_Section.History;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.Education_Section.Edu_User_Request;
import com.example.tathastu.User_Package.Education_Section.edu_user_model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Edu_user_History extends AppCompatActivity {
    FloatingActionButton btn_back, btn_addreqquest;
    RecyclerView recyclerView;
    Edu_user_history_adapter eduUserHistoryAdapter;
    ArrayList<edu_user_model> edu_donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_user_history);

        recyclerView = findViewById(R.id.recycle_edu_donate_request);
        btn_back = findViewById(R.id.BTN_user_edu_history_back);
        btn_addreqquest = findViewById(R.id.BTN_edu_user_Add_request);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(Edu_user_History.this);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        edu_donor = new ArrayList<>();
        eduUserHistoryAdapter = new Edu_user_history_adapter(this,edu_donor);
        recyclerView.setAdapter(eduUserHistoryAdapter);

        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
        String userId = sharedPreferences1.getString("userId","");

        // Fetch data from Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("education");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                edu_donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    edu_user_model edu_user_model = dataSnapshot.getValue(edu_user_model.class);

                    String userId = edu_user_model.getUserId();

                    if(userId.matches(sharedPreferences1.getString("userId","")))
                    {
                            edu_donor.add(edu_user_model);
                    }
                }
                eduUserHistoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_addreqquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Edu_user_History.this, Edu_User_Request.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Edu_user_History.this);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(Edu_user_History.this);
        finish();
    }
}