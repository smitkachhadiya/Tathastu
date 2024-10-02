package com.example.tathastu.User_Package.Food_Section.History;

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
import com.example.tathastu.User_Package.Food_Section.Food_User_Request;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Food_user_History extends AppCompatActivity {
    FloatingActionButton btn_back, btn_addreqquest;
    RecyclerView recyclerView;
    Food_user_history_adapter foodUserHistoryAdapter;
    ArrayList<food_user_model> donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_user_history);

        recyclerView = findViewById(R.id.recycle_food_donate_request);
        btn_back = findViewById(R.id.BTN_user_food_history_back);
        btn_addreqquest = findViewById(R.id.BTN_food_user_Add_request);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Animatoo.INSTANCE.animateSlideRight(Food_user_History.this);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        donor = new ArrayList<>();
        foodUserHistoryAdapter = new Food_user_history_adapter(this, donor);
        recyclerView.setAdapter(foodUserHistoryAdapter);

        SharedPreferences sharedPreferences1 = getSharedPreferences("USER",MODE_PRIVATE);
        String userId = sharedPreferences1.getString("userId","");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("food");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food_user_model data = dataSnapshot.getValue(food_user_model.class);

                    String userId = data.getUserId();

                    if(userId.matches(sharedPreferences1.getString("userId","")))
                    {
                        donor.add(data);
                    }
                }
                foodUserHistoryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_addreqquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Food_user_History.this, Food_User_Request.class);
                startActivity(i);
                Animatoo.INSTANCE.animateSlideLeft(Food_user_History.this);
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