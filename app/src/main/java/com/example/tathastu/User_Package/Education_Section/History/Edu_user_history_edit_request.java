package com.example.tathastu.User_Package.Education_Section.History;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.tathastu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Edu_user_history_edit_request extends AppCompatActivity {
    FloatingActionButton btn_back;
    TextInputEditText txt_name,txt_location,txt_note,txt_mno,txt_email;
    Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edu_user_history_edit_request);

        btn_back = findViewById(R.id.BTN_back);
        btn_save = findViewById(R.id.BTN_food_user_update);
        txt_name = findViewById(R.id.txt_name);
        txt_location = findViewById(R.id.txt_location);
        txt_note = findViewById(R.id.txt_note);
        txt_mno = findViewById(R.id.txt_mno);
        txt_email = findViewById(R.id.txt_email);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        txt_name.setText(name);
        String mno = intent.getStringExtra("mno");
        txt_mno.setText(mno);
        String email = intent.getStringExtra("email");
        txt_email.setText(email);
        String address = intent.getStringExtra("location");
        txt_location.setText(address);
        String description = intent.getStringExtra("type");
        txt_note.setText(description);
        String key = intent.getStringExtra("key");

btn_back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = txt_name.getText().toString();
                String mno = txt_mno.getText().toString();
                String email = txt_email.getText().toString();
                String address = txt_location.getText().toString();
                String desc = txt_note.getText().toString();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("education");

                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("email",email);
                map.put("mobile", mno);
                map.put("address",address);
                map.put("description",desc);

                reference.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.exists()) {

                            reference.child(key).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(Edu_user_history_edit_request.this, "Request Saved.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Edu_user_history_edit_request.this, Edu_user_History.class);
                                    startActivity(i);
                                    Animatoo.INSTANCE.animateSlideRight(Edu_user_history_edit_request.this);
                                    Toast.makeText(Edu_user_history_edit_request.this, "Updated Successfully.", Toast.LENGTH_SHORT).show();

                                }
                            });

                        } else {

                            Toast.makeText(Edu_user_history_edit_request.this, "Data is Not Updated.", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}