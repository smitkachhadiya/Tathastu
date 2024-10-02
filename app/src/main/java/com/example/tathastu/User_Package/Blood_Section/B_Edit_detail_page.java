package com.example.tathastu.User_Package.Blood_Section;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class B_Edit_detail_page extends AppCompatActivity {

    FloatingActionButton btn_back;
    TextInputEditText txt_name, txt_age, txt_weight, txt_location, txt_note, txt_mno;
    MaterialAutoCompleteTextView txt_type;
    Button btn_save;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedit_detail_page);


        btn_back = findViewById(R.id.BTN_back);
        btn_save = findViewById(R.id.BTN_save);
        txt_name = findViewById(R.id.txt_name);
        txt_age = findViewById(R.id.txt_age);
        txt_weight = findViewById(R.id.txt_weight);
        txt_type = findViewById(R.id.txt_type);
        txt_location = findViewById(R.id.txt_location);
        txt_note = findViewById(R.id.txt_note);
        txt_mno = findViewById(R.id.txt_mno);

        // Define the blood groups array within the same class
        String[] bloodGroups = new String[]{
                "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"
        };

        // Create an ArrayAdapter using the blood groups array and a default layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                bloodGroups
        );

        // Set the adapter to the AutoCompleteTextView
        txt_type.setAdapter(adapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Animatoo.INSTANCE.animateSlideRight(B_Edit_detail_page.this);
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        txt_name.setText(name);
        String mno = intent.getStringExtra("mno");
        txt_mno.setText(mno);
        String weight = intent.getStringExtra("weight");
        txt_weight.setText(weight);
        String blood_group = intent.getStringExtra("blood_group");
        txt_type.setText(blood_group);
        String age = intent.getStringExtra("age");
        txt_age.setText(age);
        String address = intent.getStringExtra("location");
        txt_location.setText(address);
        String description = intent.getStringExtra("type");
        txt_note.setText(description);
        String key = intent.getStringExtra("key");

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = txt_name.getText().toString();
                String mno = txt_mno.getText().toString();
                String age = txt_age.getText().toString();
                String address = txt_location.getText().toString();
                String weight = txt_weight.getText().toString();
                String blood_group = txt_type.getText().toString();
                String desc = txt_note.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("blood");

                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("age", age);
                map.put("mobile", mno);
                map.put("address", address);
                map.put("blood_group", blood_group);
                map.put("weight", weight);
                map.put("description", desc);

                reference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            reference.child(key).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(B_Edit_detail_page.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Animatoo.INSTANCE.animateSlideRight(B_Edit_detail_page.this);                                }
                            });

                        } else {
                            Animatoo.INSTANCE.animateSlideRight(B_Edit_detail_page.this);
                            Toast.makeText(B_Edit_detail_page.this, "Data is Not Updated.", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.INSTANCE.animateSlideRight(B_Edit_detail_page.this);
        finish();
    }
}