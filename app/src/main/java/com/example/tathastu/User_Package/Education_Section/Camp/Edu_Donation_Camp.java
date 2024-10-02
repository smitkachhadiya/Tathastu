package com.example.tathastu.User_Package.Education_Section.Camp;

import android.os.Bundle;
import android.view.View;

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

public class Edu_Donation_Camp extends AppCompatActivity {
    FloatingActionButton btn_back;
    RecyclerView recyclerView;
    Edu_Camp_Adapter adapter;
    ArrayList<NGO_food_History_model> e_donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_donation_camp);

        btn_back = findViewById(R.id.BTN_edu_user_back);
        recyclerView = findViewById(R.id.recycle_edu_camp);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(Edu_Donation_Camp.this);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        e_donor = new ArrayList<>();
        adapter = new Edu_Camp_Adapter(this,e_donor);
        recyclerView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo_education");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                e_donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NGO_food_History_model data = dataSnapshot.getValue(NGO_food_History_model.class);

                    e_donor.add(data);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(this);
        finish();
    }
}