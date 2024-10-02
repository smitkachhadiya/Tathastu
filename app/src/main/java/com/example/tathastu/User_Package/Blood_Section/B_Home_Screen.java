package com.example.tathastu.User_Package.Blood_Section;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.NGO_Package.NGO_Food_Camp.History.NGO_food_History_model;
import com.example.tathastu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class B_Home_Screen extends AppCompatActivity {

    TextView txtall;
    FloatingActionButton btn_back, btn_request;
    RecyclerView recyclerView, recyclerView2;
    adapter1 adapter1;
    adapter2 adapter2;
    ArrayList<blood_user_model> b_donor;
    ArrayList<NGO_food_History_model> b_donor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhome_screen);

        txtall = findViewById(R.id.txt_all);
        btn_back = findViewById(R.id.BTN_back);
        btn_request = findViewById(R.id.BTN_request);
        recyclerView = findViewById(R.id.recycle_blood_requirements);
        recyclerView2 = findViewById(R.id.recycle_camp_requirements);

        txtall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(B_Home_Screen.this, B_Home_Screen_2.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(B_Home_Screen.this);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(B_Home_Screen.this);
            }
        });

        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(B_Home_Screen.this, B_Request_page.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(B_Home_Screen.this);
            }
        });

        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
        String userId = sharedPreferences1.getString("userId","");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        b_donor = new ArrayList<>();
        adapter1 = new adapter1(this,b_donor);
        recyclerView.setAdapter(adapter1);

        // Fetch data from Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("blood");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                b_donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    blood_user_model blood_user_model = dataSnapshot.getValue(blood_user_model.class);

                    String userId = blood_user_model.getUserId();

                    if(!userId.matches(sharedPreferences1.getString("userId","")))
                    {
                        b_donor.add(blood_user_model);
                    }
                }
                adapter1.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView2.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        b_donor1 = new ArrayList<>();
        adapter2 = new adapter2(this,b_donor1);
        recyclerView2.setAdapter(adapter2);


        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("ngo_blood");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                b_donor1.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NGO_food_History_model data = dataSnapshot.getValue(NGO_food_History_model.class);

                    b_donor1.add(data);

                }
                adapter2.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Animatoo.INSTANCE.animateSlideRight(B_Home_Screen.this);
        finish();
    }
}



