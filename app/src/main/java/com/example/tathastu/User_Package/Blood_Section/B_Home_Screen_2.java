package com.example.tathastu.User_Package.Blood_Section;

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

public class B_Home_Screen_2 extends AppCompatActivity {

    FloatingActionButton btn_back, btn_request;
    RecyclerView recyclerView;
    adapter2 adapter2;
    ArrayList<NGO_food_History_model> b_donor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bhome_screen2);

        btn_back = findViewById(R.id.BTN_back);
        recyclerView = findViewById(R.id.recycle_camp_requirements);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

                Animatoo.INSTANCE.animateSlideRight(B_Home_Screen_2.this);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        b_donor = new ArrayList<>();
        adapter2 = new adapter2(this,b_donor);
        recyclerView.setAdapter(adapter2);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ngo_blood");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                b_donor.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    NGO_food_History_model data = dataSnapshot.getValue(NGO_food_History_model.class);

                    b_donor.add(data);

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

        Animatoo.INSTANCE.animateSlideRight(B_Home_Screen_2.this);
        finish();
    }
}