package com.example.tathastu.NGO_Package.NGO_Food_Camp.History;

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

public class NGO_food_camp_history extends AppCompatActivity {

    FloatingActionButton btn_back;
    RecyclerView recyclerView;
    NGO_food_camp_historyadapter adapter;
    ArrayList<NGO_food_History_model> c_donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_food_camp_history);
        btn_back = findViewById(R.id.BTN_back);
        recyclerView = findViewById(R.id.recycle_food_camp_history);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();   Animatoo.INSTANCE.animateSlideRight(NGO_food_camp_history.this);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        c_donor = new ArrayList<>();
        adapter = new NGO_food_camp_historyadapter(this,c_donor);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
        String userId = sharedPreferences1.getString("userId","");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo_food");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                c_donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NGO_food_History_model data = dataSnapshot.getValue(NGO_food_History_model.class);

                    String userId = data.getUserId();

                    if(userId.matches(sharedPreferences1.getString("userId","")))
                    {
                        c_donor.add(data);
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