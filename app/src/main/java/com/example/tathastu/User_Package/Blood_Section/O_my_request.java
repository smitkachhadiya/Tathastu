package com.example.tathastu.User_Package.Blood_Section;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tathastu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class O_my_request extends AppCompatActivity {

    FloatingActionButton btn_back, btn_addreqquest;
    RecyclerView recyclerView;
    adapter4 adapter4;
    ArrayList<String> item1, item2, item3, item4, item5;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_omy_request);

        recyclerView = findViewById(R.id.recycle_blood_request);
        btn_back = findViewById(R.id.BTN_back);
        btn_addreqquest = findViewById(R.id.BTN_Add_request);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        item1 = new ArrayList<>();
        item1.add("Shree Vallabhipur Taluka Patidar Pragati Mandal");
        item1.add("Smt P.J Koshiya Charitable Trust");

        item2 = new ArrayList<>();
        item2.add("21-03-2024");
        item2.add("18-04-2024");

        item3 = new ArrayList<>();
        item3.add("22-03-2024");
        item3.add("21-04-2024");

        item4 = new ArrayList<>();
        item4.add("+9193277-18627");
        item4.add("+1-539-9594");

        item5 = new ArrayList<>();
        item5.add("KATARGAM");
        item5.add("ADAJAN");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter4 = new adapter4(this,item1,item2,item3,item4,item5);
        recyclerView.setAdapter(adapter4);

        btn_addreqquest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(O_my_request.this, O_add_details.class);
                startActivity(i);

            }
        });
    }
}