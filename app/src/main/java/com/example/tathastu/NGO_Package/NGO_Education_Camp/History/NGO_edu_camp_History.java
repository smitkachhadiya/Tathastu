package com.example.tathastu.NGO_Package.NGO_Education_Camp.History;

import android.content.SharedPreferences;
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

public class NGO_edu_camp_History extends AppCompatActivity {
    FloatingActionButton btn_back;
    RecyclerView recyclerView;
    NGO_edu_camp_historyAdapter adapter;
    ArrayList<NGO_food_History_model> e_donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_edu_camp_history);
        btn_back = findViewById(R.id.BTN_back);
        recyclerView = findViewById(R.id.recycle_edu_camp_history);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Animatoo.INSTANCE.animateSlideRight(NGO_edu_camp_History.this);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        e_donor = new ArrayList<>();
        adapter = new NGO_edu_camp_historyAdapter(this,e_donor);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
        String userId = sharedPreferences1.getString("userId","");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo_education");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                e_donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NGO_food_History_model data = dataSnapshot.getValue(NGO_food_History_model.class);

                    String userId = data.getUserId();

                    if(userId.matches(sharedPreferences1.getString("userId","")))
                    {
                        e_donor.add(data);
                    }
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