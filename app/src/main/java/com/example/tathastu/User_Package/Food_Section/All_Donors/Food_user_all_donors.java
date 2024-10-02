package com.example.tathastu.User_Package.Food_Section.All_Donors;

import android.content.SharedPreferences;
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

public class Food_user_all_donors extends AppCompatActivity {
    FloatingActionButton btn_back;
    RecyclerView recyclerView;
    Food_user_alldonorAdapter adapter;  // Change the adapter type to NGO_food_donorAdapter
    ArrayList<food_user_model> f_donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_user_all_donors);

        btn_back = findViewById(R.id.BTN_user_food_history_back);
        recyclerView = findViewById(R.id.recycle_user_allfooddonor_request);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(Food_user_all_donors.this);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        f_donor = new ArrayList<>();
        adapter = new Food_user_alldonorAdapter(this, f_donor);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
        String userId = sharedPreferences1.getString("userId","");

        // Fetch data from Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("food");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                f_donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food_user_model data = dataSnapshot.getValue(food_user_model.class);

                    String userId = data.getUserId();

                    if(!userId.matches(sharedPreferences1.getString("userId","")))
                    {
                        f_donor.add(data);
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
        Animatoo.INSTANCE.animateSlideRight(Food_user_all_donors.this);
    finish();
    }
}
