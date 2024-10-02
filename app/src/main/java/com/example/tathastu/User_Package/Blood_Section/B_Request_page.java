package com.example.tathastu.User_Package.Blood_Section;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class B_Request_page extends AppCompatActivity {

    FloatingActionButton btn_back, btn_addreqquest;
    RecyclerView recyclerView;
    adapter3 adapter3;
    ArrayList<blood_user_model> b_donor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brequest_page);

        recyclerView = findViewById(R.id.recycle_blood_request);
        btn_back = findViewById(R.id.BTN_back);
        btn_addreqquest = findViewById(R.id.BTN_Add_request);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Animatoo.INSTANCE.animateSlideRight(B_Request_page.this);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        b_donor = new ArrayList<>();
        adapter3 = new adapter3(this,b_donor);
        recyclerView.setAdapter(adapter3);

        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
        String userId = sharedPreferences1.getString("userId","");

        // Fetch data from Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("blood");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                b_donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    blood_user_model data = dataSnapshot.getValue(blood_user_model.class);

                    String userId = data.getUserId();

                    if(userId.matches(sharedPreferences1.getString("userId","")))
                    {
                        b_donor.add(data);
                    }
                }
                adapter3.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        databaseReference.child(userId).child("ngo").child("blood").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                b_donor.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    blood_user_model data = dataSnapshot.getValue(blood_user_model.class);
//                    b_donor.add(data);
//                }
//                adapter3.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(B_Request_page.this, "Failed to load donors.", Toast.LENGTH_SHORT).show();
//            }
//        });


        btn_addreqquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(B_Request_page.this, B_add_details.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(B_Request_page.this);

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(B_Request_page.this);
        finish();
    }
}