package com.example.tathastu.NGO_Package.NGO_Food_Camp.Donor;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.example.tathastu.User_Package.Food_Section.History.food_user_model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NGO_food_user_request extends AppCompatActivity {
    FloatingActionButton btn_back;
    RecyclerView recyclerView;
    NGO_food_donorAdapter adapter;  // Change the adapter type to NGO_food_donorAdapter
    ArrayList<food_user_model> donor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_food_user_request);

        btn_back = findViewById(R.id.BTN_back);
        recyclerView = findViewById(R.id.recycle_food_user_request);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();   Animatoo.INSTANCE.animateSlideLeft(NGO_food_user_request.this);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        donor = new ArrayList<>();
        adapter = new NGO_food_donorAdapter(this, donor);
        recyclerView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("food");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food_user_model data = dataSnapshot.getValue(food_user_model.class);

                        donor.add(data);

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
